<script>
export default {
  name: 'FloatingAiButton',
  computed: {
    show() {
      return this.$route.meta.ai === true;
    }
  }
}
</script>

<template>
  <div v-if="show" class="ai-button-wrap">
    <button class="ai-button" @click="$emit('toggle')">
      AI Chat
    </button>
  </div>
</template>

<style scoped>
.ai-button-wrap {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 1000;
}

.ai-button {
  width: 56px;
  height: 56px;
  border: none;
  border-radius: 50%;
  background: #006bcc;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  transition: transform 0.2s ease, background 0.2s ease;
}

.ai-button:hover {
  background: #005bb5;
  transform: scale(1.1);
}
</style>
