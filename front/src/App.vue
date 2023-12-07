<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>

  <body>
  <header>

  </header>
  <main>
    <RouterView />
  </main>
  </body>
</template>

<style scoped>

header {
  background: white;
}
body {
  background: white;
  min-width: 95vw;
  display: flow;
  justify-content: center;
  align-items: center;

}
</style>
