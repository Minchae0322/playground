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

// Extract tokens from URL parameters
onMounted(() => {
  accessToken.value = (new URL(location.href)).searchParams.get('access_token');
  refreshToken.value = (new URL(location.href)).searchParams.get('refresh_token');

  // Store tokens in localStorage
  if (accessToken.value && refreshToken.value) {
    localStorage.setItem("accessToken", accessToken.value);
    localStorage.setItem("refreshToken", refreshToken.value);
  }
});

// Check if the token exists, redirect accordingly
onMounted(() => {
  if (accessToken.value) {
    sessionStorage.setItem('accessToken', accessToken.value);
    window.alert('로그인하였습니다');
    router.push({ path: '/login' });
  } else {
    router.replace('/login');
  }
});
</script>

<style scoped>

</style>
