<script setup>
import { RouterLink, RouterView } from 'vue-router'
import axios from "axios";
import {onMounted, ref} from "vue";
import defaultImage from '../src/assets/img.png';
import {useRouter} from "vue-router";
const router = useRouter();

const apiBaseUrl = "http://localhost:8080";

const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})

onMounted(async () => {
  // Check if the initial page number is provided in the route query
  await getUserInfo()
});

const clickUserInfo = function () {
  validateAccessToken();
  router.push({name: 'userInfo'})
};

const getUserInfo = async function () {
  await validateAccessToken()
  await axios.get(`${apiBaseUrl}/user/info`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    user.value.userNickname = response.data.userNickname
    user.value.userProfileImg = `data:image/jpeg;base64,${response.data.userProfileImg}`;
  });
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

<template>
  <header>
    <div>
      <img :src="user.userProfileImg || defaultImage" @click="clickUserInfo">
    </div>
  </header>

  <body>
  <main>

    <RouterView class="" />
  </main>
  </body>
</template>

<style scoped>



body {
  background: var(--background-color);
  min-width: 97vw;
  display: flow;
  justify-content: center;
  align-items: center;

}

header {
  background: var(--background-color);
  display: flex;
  align-items: end;
  justify-content: end;
}

img {
  margin:  5px 10px;
  width: 50px;
  height: 50px;
  background-color: #eee;
  border-radius: 50%;
  display: flex;
  align-items: end;
  justify-content: end;
  box-shadow: 0 3px 6px 0 rgba(29,34,53,.08);
}
</style>
