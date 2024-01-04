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

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getUserInfo()
});

const clickUserInfo = function () {
  validateAccessToken();
  router.push({name: 'userInfo'})
};

const getUserInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/user/info`,
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

@font-face {
  font-family: 'primary-font';
  src: url("assets/font/static/NotoSansKR-Regular.ttf");
}

@font-face {
  font-family: 'gothic';
  src: url("assets/font/malgun.ttf");
}

@font-face {
  font-family: 'gothic-bold';
  src: url("assets/font/malgunbd.ttf");
}

body {
  background: white;
  min-width: 97vw;
  display: flow;
  justify-content: center;
  align-items: center;

}

header {
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
