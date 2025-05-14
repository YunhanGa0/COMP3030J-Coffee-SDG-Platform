<script>
import axios from 'axios'
export default {
  name: 'AiChatModal',
  props: {
    visible: Boolean,
    resource: { type: String, required: true },
    mode:     { type: String, required: true },
    targetId: { type: [String, Number], default: null },
    dataList: { type: Array, default: () => [] },
    filters:  { type: Object, default: () => ({}) },
  },
  data() {
    return {
      history: [],
      userInput: '',
      loading: false
    }
  },
  methods: {
    close() {
      this.$emit('update:visible', false)
    },
    async send() {
      const text = this.userInput.trim()
      if (!text) return
      this.history.push({ role: 'user', content: text })
      this.loading = true

      const payload = {
        resource: this.resource,
        mode:     this.mode,
        targetId: this.targetId,
        filters:  this.filters,
        query:    text
      }
      try {
        const res = await axios.post('/api/ai/query', payload)
        const answer = res.data.answer || res.data.result
        this.history.push({ role: 'assistant', content: answer })
        this.$nextTick(() => {
          this.$refs.bottom.scrollIntoView({ behavior: 'smooth' })
        })
      } catch (e) {
        this.history.push({ role: 'assistant', content: '请求失败，请重试' })
      } finally {
        this.userInput = ''
        this.loading = false
      }
    }
  }
}
</script>

<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container">
      <header class="modal-header">
        <h3>AI Chat</h3>
        <button class="modal-close" @click="close">&times;</button>
      </header>
      <section class="modal-body">
        <div class="messages">
          <div
            v-for="(m, i) in history"
            :key="i"
            :class="['message', m.role === 'user' ? 'msg-user' : 'msg-bot']"
          >
            {{ m.content }}
          </div>
          <div ref="bottom"></div>
        </div>
      </section>
      <footer class="modal-footer">
        <textarea
          v-model="userInput"
          class="input-textarea"
          placeholder="Input Your Question..."
          @keydown.enter.prevent="send"
        ></textarea>
        <button class="send-button" :disabled="loading" @click="send">
          {{ loading ? 'Sending…' : 'Send' }}
        </button>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right:0; bottom:0;
  background: rgba(0,0,0,0.4);
  display: flex; align-items: center; justify-content: center;
  z-index: 1100;
}
.modal-container {
  width: 400px;
  max-height: 80vh;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 20px rgba(0,0,0,0.2);
}
.modal-header {
  padding: 12px 16px;
  background: #f5f5f5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.modal-header h3 {
  margin: 0;
  font-size: 16px;
}
.modal-close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}
.modal-body {
  padding: 8px 16px;
  overflow-y: auto;
  flex: 1;
}
.messages {
  display: flex;
  flex-direction: column;
}
.message {
  max-width: 80%;
  margin: 4px 0;
  padding: 8px 12px;
  border-radius: 4px;
  line-height: 1.4;
}
.msg-user {
  align-self: flex-end;
  background: #e1f5fe;
}
.msg-bot {
  align-self: flex-start;
  background: #f1f1f1;
}
.modal-footer {
  padding: 8px 16px;
  border-top: 1px solid #ddd;
  display: flex;
  flex-direction: column;
}
.input-textarea {
  width: 100%;
  height: 60px;
  resize: none;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}
.send-button {
  margin-top: 8px;
  align-self: flex-end;
  padding: 6px 16px;
  border: none;
  background: #006bcc;
  color: #fff;
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
}
.send-button:disabled {
  background: #9ecffa;
  cursor: not-allowed;
}
</style>
