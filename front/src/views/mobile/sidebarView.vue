<template>

  <nav class="navigation">
    <ul class="nav-links">
      <li>
        <div @click="toggleMenu('home', $event)" class="menu-container"
             :class="{'active-menu-container': menu === 'home'}">
          <RouterLink :to="{ name:'home'}" class="sub-nav-item">
            <img class="icon" src='@/assets/icon-home.png'>
          </RouterLink>
        </div>
      </li>
      <li>
        <div @click="toggleMenu('playground', $event)" class="menu-container"
             :class="{'active-menu-container': menu === 'playground'}">
          <img class="icon" src='@/assets/playground-icon.png'>

        </div>
        <ul @click="clickRouter" :style="{ left: calculateLeftPosition(0.35) }" v-if="menu === 'playground'" class="sub-menu">
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
        <div @click="toggleMenu('team', $event)" class="menu-container"
             :class="{'active-menu-container': menu === 'team'}">
          <img class="icon" src='@/assets/team-icon.png'>

        </div>
        <ul @click="clickRouter" v-if="menu === 'team'" :style="{ left: calculateLeftPosition(1.1) }" class="sub-menu">

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
        <div @click="toggleMenu('game', $event)" class="menu-container"
             :class="{'active-menu-container': menu === 'game'}">
          <img class="icon" src='@/assets/game-icon.png'>

        </div>
        <ul v-if="menu === 'game'" @click="clickRouter" :style="{ left: calculateLeftPosition(1.85) }" class="sub-menu">
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
      <li class="user-info-container" @click="clickUserInfo">
        <img class="user-profile-img" :src="user.userProfileImg || defaultImage">
      </li>
    </ul>
  </nav>
</template>


<script setup>
import {getCurrentInstance, onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from '@/assets/icon-default.png';

const router = useRouter();
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})
const isLoggedIn = ref(false);

const menuItems = ['home','playground', 'team', 'game']; // 메뉴 항목들
const menuCount = menuItems.length;


onMounted(async () => {
  await getUserInfo();
});

const clickRouter = async () => {
  menu.value = "";
}

const calculateLeftPosition = (index) => {
  return `${(100 / menuCount) * index}%`;
};

const menu = ref("");

const toggleMenu = (selectedMenu, event) => {
  event.stopPropagation();
  if(menu.value === selectedMenu) {
    menu.value = "";
  } else {
    menu.value = selectedMenu;
  }
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
          }
        }
    );
    isLoggedIn.value = true;
    user.value.userNickname = response.data.userNickname;
    user.value.userProfileImg = `data:image/jpeg;base64,${response.data.userProfileImg}`;
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

<style scoped>
a {
  text-decoration: none;
  font-family: MiSans-Medium, sans-serif;
  color: var(--text-hint);
}

a:hover {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}

.navigation {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-around;
  background-color: #fff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.nav-links {
  display: flex; /* 항목들을 가로로 배치 */
  padding: 0;
  margin: 0;
  width: 100%; /* 전체 너비를 사용 */
}

li {
  list-style: none;
}

.nav-links li {
  flex: 1; /* 모든 항목이 동일한 공간을 차지하도록 함 */
  text-align: center; /* 텍스트를 중앙으로 정렬 */
}

.icon {
  width: 24px;
  height: 24px;
  display: block;
  margin: 10px auto;
  opacity: 0.4;
}

.user-info-container {
  margin: 10px auto;
  display: block;
}

.user-profile-img {
  width: 24px;
  height: 24px;
  background-color: #eee;
  border-radius: 50%;

}

.menu-container {
  position: relative; /* 이 요소를 기준점으로 설정 */
}

.active-menu-container .icon {
  opacity: 1;
}

.sub-menu {
  position: absolute;
  left: 50%; /* 왼쪽에서 50% 떨어진 곳에 위치 */
  transform: translateX(-50%); /* X축 중앙 정렬 */
  bottom: 50px; /* 아이콘 아래에 위치하도록 bottom 값을 조정 */
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  padding: 10px;
  display: none; /* 기본적으로 보이지 않도록 설정 */
  transition: all 0.3s ease;
  z-index: 100; /* 다른 요소들 위에 보이도록 z-index 설정 */
}

/* 서브메뉴를 보여주는 스타일 */
.menu-container.active-menu-container + .sub-menu {
  display: block;
}






.router-link-active {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}
</style>