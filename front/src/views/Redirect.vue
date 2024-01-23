<template>
  <body>
  <div>
    <el-text>{{ accessToken }}</el-text>
    <el-text>바봅야</el-text>
  </div>

  </body>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const accessToken = ref(null);
const refreshToken = ref(null);


onMounted(async () => {
  accessToken.value = (new URL(location.href)).searchParams.get('access_token');
  refreshToken.value = (new URL(location.href)).searchParams.get('refresh_token');

  console.log(accessToken.value)
  // Store tokens in localStorage
  if (accessToken.value && refreshToken.value) {
    localStorage.setItem("accessToken", accessToken.value);
    localStorage.setItem("refreshToken", refreshToken.value);
  }
  await router.push({ path: '/home' });
});
</script>

<style scoped>

</style>
