<script setup>
import {RouterLink, RouterView} from 'vue-router'
import axios from "axios";
import {getCurrentInstance, onMounted, onUnmounted, ref} from "vue";
import defaultImage from '@/assets/img.png';
import {useRouter} from "vue-router";
import mSidebarView from "../src/views/mobile/sidebarView.vue"

const router = useRouter();
const isMobile = ref(window.innerWidth < 650);

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

const user = ref({
  userNickname: '',
  userProfileImg: ref(defaultImage)
})
const isLoggedIn = ref(false);

const accessToken = ref(null);
const refreshToken = ref(null);


onMounted(async () => {
  window.addEventListener('resize', updateWindowWidth);
  await setToken()
  await getUserInfo();
});

onUnmounted(() => {
  window.removeEventListener('resize', updateWindowWidth);
});

const updateWindowWidth = async () => {
  isMobile.value = window.innerWidth < 600;
};

const menuVisible = ref({
  playground: false,
  team: false,
  game: false,
});

const setToken = async () => {
  if (isLoggedIn.value) {
    return
  }
  accessToken.value = (new URL(location.href)).searchParams.get('access_token');
  refreshToken.value = (new URL(location.href)).searchParams.get('refresh_token');

  console.log(accessToken.value)
  console.log(refreshToken.value)
  // Store tokens in localStorage
  if (accessToken.value && refreshToken.value) {
    localStorage.setItem("accessToken", accessToken.value);
    localStorage.setItem("refreshToken", refreshToken.value);
    isLoggedIn.value = true;
    await router.push({path: '/home'});
    return
  }
  isLoggedIn.value = true;

};
const toggleMenu = (menu) => {
  menuVisible.value[menu] = !menuVisible.value[menu];
};

const clickUserInfo = function () {
  validateAccessToken();
  router.push({name: 'userInfo'})
};

const logout = async () => {
  const isConfirm = confirm("确定要退出吗？")
  if(!isConfirm) {
    return
  }

  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/user/logout`, {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    if (response.data === true) {
      isLoggedIn.value = false
      await router.push({name: 'login'})
    }
  } catch (error) {
    alert(error.response.data.message)
  }
}

const getUserInfo = async () => {
  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/user/info`, {
          headers: {
            'Authorization': localStorage.getItem("accessToken"),
          }
        }
    );
    isLoggedIn.value = true;
    user.value.userNickname = response.data.userNickname;
    user.value.userProfileImg = response.data.userProfileImg ? `data:image/jpeg;base64,${response.data.userProfileImg}` : defaultImage;
  } catch (error) {
    isLoggedIn.value = false;
  }
};

const validateAccessToken = async () => {
  const accessToken = getAccessToken();
  if (!accessToken) {
    await redirectToLogin()
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
      headers: {'refreshToken': refreshToken}
    });
    if (response.status === 200) {
      const newAccessToken = response.headers['authorization'];
      localStorage.setItem('accessToken', newAccessToken);
      return newAccessToken;
    }
  } catch (error) {
    await redirectToLogin();
  }
};


const redirectToLogin = async () => {
  await router.push({name: 'login'});
};
</script>

<template>

  <head>

  </head>
  <main v-if="isMobile">
    <div class="router-view-container-m">
      <RouterView/>
    </div>
    <component v-if="$route.name !== 'login'" :class="{'disable-sidebar': $route.name === 'login' || $route.name === 'signup'}" class="under-bar"
               :is="mSidebarView"></component>
  </main>

  <main v-else class="main-content">
    <div v-if="$route.name !== 'login'" :class="{'disable-sidebar': $route.name === 'login' || $route.name === 'signup'}" class="sidebar">
      <router-link :to="{name : 'home'}">
        <div class="school-info-container">
          <img class="school-profile-img" src="../src/assets/school-profile.jpeg">
          <div class="school-name">哈尔滨工业大学</div>
        </div>
      </router-link>
      <div class="user-info-container" @click="clickUserInfo">
        <img class="user-profile-img" :src="user.userProfileImg || defaultImage">
        <div class="user-name">{{ user.userNickname }}</div>
        <div @click="logout" class="logout">logout</div>
      </div>
      <nav class="navigation">
        <ul class="nav-links">
          <li>
            <div @click="toggleMenu('playground')" class="menu-container"
                 :class="{'active-menu-container': menuVisible.playground}">
              <img class="icon" src='../src/assets/playground-icon.png'>
              <div class="nav-item">运动场</div>
            </div>
            <ul v-if="menuVisible.playground" class="sub-menu">

              <RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'SOCCER'}}" class="sub-nav-item">
                <li>足球</li>
              </RouterLink>
              <RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'BASKETBALL'}}" class="sub-nav-item">
                <li>篮球</li>
              </RouterLink>
              <RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'BADMINTON'}}" class="sub-nav-item">
                <li>羽毛球</li>
              </RouterLink>
              <RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'TABLE_TENNIS'}}" class="sub-nav-item">
                <li>乒乓球</li>
              </RouterLink>
              <RouterLink :to="{ name:'playgroundList', params: {sportsEvent : 'TENNIS'}}" class="sub-nav-item">
                <li>网球</li>
              </RouterLink>


            </ul>
          </li>
          <li>
            <div @click="toggleMenu('team')" class="menu-container"
                 :class="{'active-menu-container': menuVisible.team}">
              <img class="icon" src='../src/assets/team-icon.png'>
              <div class="nav-item">队伍</div>
            </div>
            <ul v-if="menuVisible.team" class="sub-menu">

              <RouterLink :to="{ name:'myTeam'}" class="sub-nav-item">
                <li>我的队伍</li>
              </RouterLink>
              <RouterLink :to="{ name:'teamList'}" class="sub-nav-item">
                <li>加入队伍</li>
              </RouterLink>
              <RouterLink :to="{ name:'teamRequest'}" class="sub-nav-item">
                <li>队伍加入请求</li>
              </RouterLink>
              <RouterLink :to="{ name:'teamBuilding'}" class="sub-nav-item">
                <li>组队</li>
              </RouterLink>

            </ul>
          </li>
          <li>
            <div @click="toggleMenu('game')" class="menu-container"
                 :class="{'active-menu-container': menuVisible.game}">
              <img class="icon" src='../src/assets/game-icon.png'>
              <div class="nav-item">比赛</div>
            </div>
            <ul v-if="menuVisible.game" class="sub-menu">
              <RouterLink :to="{name: 'gameRequest'}" class="sub-nav-item">
                <li>比赛参加请求</li>
              </RouterLink>
              <RouterLink :to="{name: 'myGame'}" class="sub-nav-item">
                <li>我的比赛</li>
              </RouterLink>
              <RouterLink :to="{name: 'hostGame'}" class="sub-nav-item">
                <li>我主办的比赛</li>
              </RouterLink>
            </ul>
          </li>
        </ul>
      </nav>

    </div>
    <div :class="{'login-router-view ': $route.name === 'login' || $route.name === 'signup'}" class="router-view-container">
      <RouterView/>
    </div>
  </main>
</template>

<style>
.main-content {
  display: flex;
  align-items: center;
  justify-content: center;

}

a {
  text-decoration: none;
  font-family: MiSans-Medium, sans-serif;
  color: var(--text-hint);
}


a:hover {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}

li {
  list-style: none;
}

.sidebar {
  flex: 1; /* sidebar 너비 설정 */
  min-width: 230px;
  max-width: 260px;
  width: 17%;
  min-height: 100vh;
  height: 100%;
  position: fixed; /* 화면에 고정 */
  left: 0;
  top: 0;
  background-color: #fff;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow-y: auto; /* 사이드바 내용이 많을 경우 스크롤 가능 */
}


.school-info-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  justify-content: center;
  align-content: center;
}

.school-profile-img {
  width: 90px;
  height: 85px;
  padding-right: 5px;
  margin: 20px auto 10px auto;
}

.school-name {
  text-align: center;
  color: #4c8ba8;
  font-size: 20px;
}

.user-info-container {
  display: flex;
  padding: 20px 10px;
  margin: 10px 0;
  cursor: pointer;
  background-color: var(--accent-color);

}

.user-profile-img {
  width: 40px;
  height: 40px;
  background-color: #eee;
  border-radius: 50%;

}

.user-name {
  font-size: 17px;
  font-family: MiSans-Normal, sans-serif;
  margin: auto 0px auto 20px;
  color: var(--white);
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
  font-family: MiSans-Semibold, sans-serif;
}

.router-link-active {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}

.router-view-container {
  flex: 5; /* sidebar 너비 설정 */
  height: 100%; /* 원하는 높이로 설정 */
  margin-top: 20px;
  margin-left: 250px; /* 사이드바 너비만큼 여백 추가 */
  overflow-y: auto; /* 내용이 높이를 초과하면 스크롤바 생성 */
  width: calc(100% - 250px);
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
  font-family: MiSans-Normal, sans-serif;
}

.nav-item:hover,
.nav-item.router-link-active {
  background-color: #f0f0f0;
}

.sub-menu li {
  margin: 15px 0px 15px 0px;
  list-style-type: none;
  width: 80%;
  padding: 5px;
  font-size: 14px;
  border-bottom: 1px solid #838383;
}

.sub-menu li:hover {
  color: black;
}


.logout {
  color: #d70000;
  text-align: end;
  margin: auto 20px auto auto;
  text-decoration: underline;
  font-family: MiSans-Normal, sans-serif;
}

.logout:hover {
  cursor: pointer;
}


.router-view-container-m {
  width: 100%;
  margin-top: 60px;
  margin-bottom: 60px;

}


.disable-sidebar {
  display: none;
}

.login-router-view {
  width: 100%;
  margin: 0;
}


@media (max-width: 600px) {
  .router-view-container-m {
    width: 100%;
    justify-content: center;
    align-content: center;
    margin: 80px auto;
  }
}



</style>
