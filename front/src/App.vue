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



const subMenuVisible = ref({
  home: false,
  matches: false,
  // ... other menus
});

const toggleSubMenu = (menu) => {
  subMenuVisible.value[menu] = !subMenuVisible.value[menu];
};

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



  <main class="main-content">
    <div class="sidebar">
      <div class="logo-container">
        <img src="../src/assets/img.png" alt="Logo" class="logo">
      </div>
      <nav class="navigation">
        <ul class="nav-links">
          <li>
            <div @click="toggleSubMenu('home')" class="nav-item">Home</div>
            <ul v-if="subMenuVisible.home" class="sub-menu">
              <li><RouterLink to="/home/1" class="sub-nav-item">Sub Menu 1</RouterLink></li>
              <li><RouterLink to="/home/2" class="sub-nav-item">Sub Menu 2</RouterLink></li>
            </ul>
          </li>
          <li>
            <div @click="toggleSubMenu('matches')" class="nav-item">Matches</div>
            <ul v-if="subMenuVisible.matches" class="sub-menu">
              <li><RouterLink to="/matches/1" class="sub-nav-item">Sub Menu 1</RouterLink></li>
              <li><RouterLink to="/matches/2" class="sub-nav-item">Sub Menu 2</RouterLink></li>
            </ul>
          </li>
          <!-- ... 다른 메뉴 아이템 ... -->
        </ul>
      </nav>
    </div>
    <div class="router-view-container">
    <RouterView />
    </div>
  </main>
</template>

<style scoped>
.main-content {
  display: flex;
  align-items: start;
  justify-content: start;

}


.sidebar {
  flex: 1; /* sidebar 너비 설정 */
  width: 250px;
  height: 92vh;
  left: 0;
  top: 0;
  background-color: #fff;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.router-view-container {
  flex: 5; /* sidebar 너비 설정 */
  height: 88vh; /* 원하는 높이로 설정 */
  margin-top: 20px;
  overflow-y: auto; /* 내용이 높이를 초과하면 스크롤바 생성 */
}

header {
  background: var(--white);
  display: flex;
  align-items: end;
  height: 8vh;
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



.logo-container {
  padding: 20px;
  text-align: center;
}

.logo {
  max-width: 100%;
  height: auto;
}

.navigation {
  flex-grow: 1;
}

.nav-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  display: block;
  padding: 10px 20px;
  text-decoration: none;
  color: #333;
  font-weight: bold;
}

.nav-item:hover,
.nav-item.router-link-active {
  background-color: #f0f0f0;
}


</style>
