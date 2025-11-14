package com.example.suapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.os.Handler
import android.os.Looper
import android.util.Log

// 用于 UI 树序列化
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

class ScreenUIAgentService: AccessibilityService() {
    private val TAG = "ScreenUIAgentService"

    // 在主线程上运行
    private val debounceHandler = Handler(Looper.getMainLooper())

    // 实例
    private var screenContentProcessing: Runnable? = null

    // 防抖间隔
    private val DEBOUNCE_DELAY_MS = 300L

/** 初始连接, 中断, 销毁 **/
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service 已连接")
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service 被中断")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Accessibility Service 已销毁")
    }

/** 事件处理核心 **/
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        Log.d(TAG, "onAccessibilityEvent: 收到无障碍事件 type=${event.eventType}, package=${event.packageName}")

        // 防抖处理
        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if(event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    Log.d(TAG, "onAccessibilityEvent: 窗口状态改变 package=${event.packageName}")
                }

                triggerDebouncedScreenAnalysis()
            }
        }
    }

    fun triggerDebouncedScreenAnalysis() {
        // 如果 runnable 还没有初始化
        if (screenContentProcessing == null) {
            screenContentProcessing = Runnable {
                Log.i(TAG, "Debounce 延迟结束, 分析 UI 树")

                // 分析 UI 树可能会存在性能问题
                val uiTreeJson = analyzeUiTree()

                // TODO: 将 uiTreeJson 传递给活动识别器 Activity Recognizer
                Log.d(TAG, "UI 树 JSON: $uiTreeJson")
            }
        }

        // 移除旧的, 待处理的 runnable
        debounceHandler.removeCallbacks(screenContentProcessing!!)
        // 延迟执行 runnable
        debounceHandler.postDelayed(screenContentProcessing!!, DEBOUNCE_DELAY_MS)
    }

/** UI 树分析和 JSON 序列化 */
    fun analyzeUiTree(): String {
        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
        if (rootNode == null) {
            Log.w(TAG, "analyzeUiTree: 无法获取根节点")
            return "{\"error\": \"无法获取根节点\"}"
        }

        val uiTree: UINode? = buildNodeTree(rootNode)

        return try{
            val  json = Json { prettyPrint = true }
                json.encodeToString(uiTree)
        } catch (e: Exception) {
            Log.e(TAG, "analyzeUiTree: 序列化 UI 树时出错", e)
            "{\"error\": \"序列化 UI 树时出错: ${e.message}\"}"
        }
    }

    fun buildNodeTree(node: AccessibilityNodeInfo?): UINode? {
        if (node == null) return null

        val children = mutableListOf<UINode>()
        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)

            if (childNode != null) {
                buildNodeTree(childNode)?.let {
                    children.add(it)
                }
            }
        }

        return UINode(
            viewId = node.viewIdResourceName?.toString()?.nullIfEmpty(),
            className = node.className?.toString()?.nullIfEmpty(),
            text = node.text?.toString()?.nullIfEmpty(),
            contentDescription = node.contentDescription?.toString()?.nullIfEmpty(),
            isClickable = node.isClickable,
            childCount = node.childCount,
            children = children
        )
    }
     
    fun String.nullIfEmpty(): String? {
        return this.ifEmpty { null }
    }
}

@Serializable
data class UINode(
    val viewId: String? = null,
    val className: String? = null,
    val text: String? = null,
    val contentDescription: String? = null,
    val isClickable: Boolean = false,
    val childCount: Int = 0,
    val children: List<UINode> = emptyList()
)
