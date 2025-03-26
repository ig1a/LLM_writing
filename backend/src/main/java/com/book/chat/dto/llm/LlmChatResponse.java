package com.book.chat.dto.llm;

import java.util.List;

/**
 * LLM聊天响应对象
 * 用于解析大模型API返回的响应格式
 * 
 * 修改功能:
 * 1. 移除Lombok依赖，改为手动实现getter/setter方法
 * 2. 为内部类Choice和Usage添加全部访问方法
 */
public class LlmChatResponse {
    /**
     * 响应ID
     */
    private String id;
    
    /**
     * 响应对象类型
     */
    private String object;
    
    /**
     * 创建时间
     */
    private Long created;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 响应内容列表
     */
    private List<Choice> choices;
    
    /**
     * 使用的token统计
     */
    private Usage usage;
    
    /**
     * 获取ID
     * @return 响应ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * 设置ID
     * @param id 响应ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * 获取对象类型
     * @return 对象类型
     */
    public String getObject() {
        return object;
    }
    
    /**
     * 设置对象类型
     * @param object 对象类型
     */
    public void setObject(String object) {
        this.object = object;
    }
    
    /**
     * 获取创建时间
     * @return 创建时间
     */
    public Long getCreated() {
        return created;
    }
    
    /**
     * 设置创建时间
     * @param created 创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }
    
    /**
     * 获取模型名称
     * @return 模型名称
     */
    public String getModel() {
        return model;
    }
    
    /**
     * 设置模型名称
     * @param model 模型名称
     */
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
     * 获取响应选择列表
     * @return 响应选择列表
     */
    public List<Choice> getChoices() {
        return choices;
    }
    
    /**
     * 设置响应选择列表
     * @param choices 响应选择列表
     */
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
    
    /**
     * 获取使用的token统计
     * @return token统计
     */
    public Usage getUsage() {
        return usage;
    }
    
    /**
     * 设置使用的token统计
     * @param usage token统计
     */
    public void setUsage(Usage usage) {
        this.usage = usage;
    }
    
    /**
     * 获取大模型的回复内容
     * 
     * @return 回复内容字符串，如果没有回复则返回空字符串
     */
    public String getReplyContent() {
        // 检查返回结果是否有效并提取内容
        if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null) {
            return choices.get(0).getMessage().getContent();
        }
        return "";
    }
    
    /**
     * 选择对象，包含消息内容
     */
    public static class Choice {
        /**
         * 序号
         */
        private Integer index;
        
        /**
         * 消息对象，包含AI回复的实际内容
         */
        private LlmMessage message;
        
        /**
         * 结束原因
         */
        private String finish_reason;
        
        /**
         * 获取序号
         * @return 序号
         */
        public Integer getIndex() {
            return index;
        }
        
        /**
         * 设置序号
         * @param index 序号
         */
        public void setIndex(Integer index) {
            this.index = index;
        }
        
        /**
         * 获取消息对象
         * @return 消息对象
         */
        public LlmMessage getMessage() {
            return message;
        }
        
        /**
         * 设置消息对象
         * @param message 消息对象
         */
        public void setMessage(LlmMessage message) {
            this.message = message;
        }
        
        /**
         * 获取结束原因
         * @return 结束原因
         */
        public String getFinish_reason() {
            return finish_reason;
        }
        
        /**
         * 设置结束原因
         * @param finish_reason 结束原因
         */
        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }
    }
    
    /**
     * 使用的token统计
     */
    public static class Usage {
        /**
         * 提示tokens数量
         */
        private Integer prompt_tokens;
        
        /**
         * 补全tokens数量
         */
        private Integer completion_tokens;
        
        /**
         * 总tokens数量
         */
        private Integer total_tokens;
        
        /**
         * 获取提示tokens数量
         * @return 提示tokens数量
         */
        public Integer getPrompt_tokens() {
            return prompt_tokens;
        }
        
        /**
         * 设置提示tokens数量
         * @param prompt_tokens 提示tokens数量
         */
        public void setPrompt_tokens(Integer prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }
        
        /**
         * 获取补全tokens数量
         * @return 补全tokens数量
         */
        public Integer getCompletion_tokens() {
            return completion_tokens;
        }
        
        /**
         * 设置补全tokens数量
         * @param completion_tokens 补全tokens数量
         */
        public void setCompletion_tokens(Integer completion_tokens) {
            this.completion_tokens = completion_tokens;
        }
        
        /**
         * 获取总tokens数量
         * @return 总tokens数量
         */
        public Integer getTotal_tokens() {
            return total_tokens;
        }
        
        /**
         * 设置总tokens数量
         * @param total_tokens 总tokens数量
         */
        public void setTotal_tokens(Integer total_tokens) {
            this.total_tokens = total_tokens;
        }
    }
}
