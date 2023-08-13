<template>
  <label :style="{ color: isValid ? 'black' : 'red' }">Elem</label>
</template>

<script>
export default {
  data() {
    return {
      color: "#ff0000",
      isValid: false
    }
  }
}
</script>
