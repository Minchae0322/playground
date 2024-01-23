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
const isLoggedIn = ref(false);

onMounted(async () => {
  // Check if the initial page number is provided in the route query
  await getUserInfo()
});

const subMenuVisible = ref({
  playground: false,
  team: false,
  game: false,
  // ... other menus
});

const toggleSubMenu = (menu) => {
  subMenuVisible.value[menu] = !subMenuVisible.value[menu];
};

const clickUserInfo = function () {
  validateAccessToken();
  router.push({name: 'userInfo'})
};

const getUserInfo = async () => {
  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/user/info`, {
      headers: {
        'Authorization': localStorage.getItem("accessToken"),
      }}
    );
    isLoggedIn.value = true;
    user.value.userNickname = response.data.userNickname;
    user.value.userProfileImg = `data:image/jpeg;base64,${response.data.userProfileImg}`;
  } catch (error) {
    isLoggedIn.value = false;
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

<template>





  <main class="main-content">
    <div v-if="$route.name !== 'login'" class="sidebar">
      <div>
        <img class="user-profile-img" :src="user.userProfileImg || defaultImage" @click="clickUserInfo">
      </div>
      <nav class="navigation">
        <ul class="nav-links">
          <li>
            <div @click="toggleSubMenu('playground')" class="menu-container" :class="{'active-menu-container': subMenuVisible.playground}">
              <img class="icon" src='../src/assets/playground-icon.png'>
              <div  class="nav-item">运动场</div>
            </div>
            <ul v-if="subMenuVisible.playground" class="sub-menu">
              <li><RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'SOCCER'}}" class="sub-nav-item">SOCCER</RouterLink></li>
              <li><RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}"  class="sub-nav-item">BASKET BALL</RouterLink></li>
              <li><RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'BADMINTON'}}"  class="sub-nav-item">BADMINTON</RouterLink></li>
            </ul>
          </li>
          <li>
            <div @click="toggleSubMenu('team')" class="menu-container" :class="{'active-menu-container': subMenuVisible.team}">
              <img class="icon" src='../src/assets/team-icon.png'>
              <div  class="nav-item">队伍</div>
            </div>
            <ul v-if="subMenuVisible.team" class="sub-menu">
              <li>
                <RouterLink :to="{ name:'myTeam'}" class="sub-nav-item">My Team</RouterLink>
              </li>
              <li><RouterLink  :to="{ name:'teamList'}" class="sub-nav-item">Team Join</RouterLink></li>
              <li><RouterLink :to="{ name:'teamRequest'}" class="sub-nav-item">Team Request</RouterLink></li>
              <li><RouterLink  :to="{ name:'teamBuilding'}" class="sub-nav-item">Make Team</RouterLink></li>
            </ul>
          </li>
          <li>
            <div @click="toggleSubMenu('game')" class="menu-container" :class="{'active-menu-container': subMenuVisible.game}">
              <img class="icon" src='../src/assets/game-icon.png'>
              <div  class="nav-item">比赛</div>
            </div>
            <ul v-if="subMenuVisible.game" class="sub-menu">
              <li><RouterLink :to="{name: 'gameRequest'}" class="sub-nav-item">Game Request</RouterLink></li>
              <li><RouterLink :to="{name: 'myGame'}"  class="sub-nav-item">My Game</RouterLink></li>
              <li><RouterLink :to="{name: 'hostGame'}"  class="sub-nav-item">Host Game</RouterLink></li>
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
  height: 100vh;
  left: 0;
  top: 0;
  background-color: #fff;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.menu-container {
  display: flex;
  opacity: 0.6;
 border-radius: 8px;
  margin: 10px;
  padding: 10px 20px;
}

.menu-container:hover {
  background-color: #f0f0f0;
  opacity: 1;
}

.active-menu-container {
  opacity: 1 !important;
  background-color: #f0f0f0;

}

.active-menu-container .nav-item {
  font-family: MiSans-Semibold,sans-serif;
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
.icon {
  width: 40px;
  height: 40px;
  margin-right: 10px;
  background-color: transparent;
}

.user-profile-img {
  margin:  70px 10px;
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
  margin: auto 5px;
  text-decoration: none;
  color: var(--text-black);
  letter-spacing: 1px;
  font-size: 16px;
  font-family: MiSans-Normal,sans-serif;
}

.nav-item:hover,
.nav-item.router-link-active {
  background-color: #f0f0f0;
}


</style>
