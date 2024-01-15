<template>

</template>

<script setup>
import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from "@/assets/img.png";
import {onMounted, ref} from "vue";

const apiBaseUrl = "http://localhost:8080";

const pastGames = ref([]);
const upcomingGames = ref([]);

onMounted(async () => {
  await getMyGame(2024, 1)
});

const getMyGame = async (year, month) => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/user/game/${year}/${month}`, {
      headers: {
        'Authorization': getAccessToken(),
      }
    });

    const currentTime = new Date();

    // response.data의 각 아이템에 대해 반복
    response.data.forEach(game => {
      // game.startTime이 현재 시간 이후라면 pastGames 배열에 추가
      // game.startTime이 현재 시간 이전이라면 upcomingGames 배열에 추가
      if (new Date(game.startTime) > currentTime) {
        pastGames.value.push(game);
      } else {
        upcomingGames.value.push(game);
      }
    });

  } catch (error) {
    console.error('게임 정보를 가져오는데 실패했습니다.', error.response.data.message);
  }
};

const validateAccessToken = async function () {
  const accessToken = getAccessToken();
  if (!accessToken) {
    redirectToLogin()
    return;
  }

  try {
    await axios.get(`${apiBaseUrl}/token/valid`, {
      headers: {'Authorization': accessToken},
    });
  } catch (error) {
    await updateAccessToken();
  }
};

const getAccessToken = function () {
  return localStorage.getItem("accessToken");
};

const updateAccessToken = async function () {
  const refreshToken = localStorage.getItem("refreshToken");
  if (!refreshToken) return redirectToLogin();

  try {
    const response = await axios.get(`${apiBaseUrl}/token/refresh`, {
      headers: { 'RefreshToken': refreshToken }
    });
    if (response.status === 200) {
      const newAccessToken = response.headers['authorization'];
      localStorage.setItem('accessToken', newAccessToken);
      return newAccessToken;
    }
  } catch (error) {
    redirectToLogin();
  }
};

const redirectToLogin = function () {
  router.push("/login");
};
</script>

<style scoped>

</style>