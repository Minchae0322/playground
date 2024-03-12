<template>
  <nav class="logo-bar">
    <div class="logo-mobile">
      <img  src='@/assets/school-profile.jpeg'>
      <div>哈尔滨工业大学</div>
    </div>
  </nav>
  <nav class="navigation-under-bar">
    <ul class="nav-links-under-bar">
      <li>
        <RouterLink :to="{ name:'home'}" class="sub-nav-item">
        <div @click="toggleMenu('home', $event)" class="menu-container-under-bar"
             :class="{'active-menu-container-under-bar': menu === 'home'}">
            <img class="icon-under-bar" src='@/assets/icon-home.png'>
        </div>
        </RouterLink>
      </li>
      <li>
        <div @click="toggleMenu('playground', $event)" class="menu-container-under-bar"
             :class="{'active-menu-container-under-bar': menu === 'playground'}">
          <img class="icon-under-bar" src='@/assets/playground-icon.png'>

        </div>
        <ul @click="clickRouter" v-if="menu === 'playground'"
            class="sub-menu-under-bar">
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
        <div @click="toggleMenu('team', $event)" class="menu-container-under-bar"
             :class="{'active-menu-container-under-bar': menu === 'team'}">
          <img class="icon-under-bar" src='@/assets/team-icon.png'>

        </div>
        <ul @click="clickRouter" v-if="menu === 'team'"
            class="sub-menu-under-bar">
          <RouterLink :to="{ name:'myTeam'}" class="sub-nav-item">
            <li>我的队伍</li>
          </RouterLink>
          <RouterLink  :to="{ name:'teamList'}" class="sub-nav-item">
            <li>加入队伍</li>
          </RouterLink>
          <RouterLink  @click="toggleMenu('team', $event)" :to="{ name:'teamRequest'}" class="sub-nav-item">
            <li>队伍加入请求</li>
          </RouterLink>
          <RouterLink  @click="toggleMenu('team', $event)" :to="{ name:'teamBuilding'}" class="sub-nav-item">
            <li>组队</li>
          </RouterLink>

        </ul>
      </li>
      <li>
        <div @click="toggleMenu('game', $event)" class="menu-container-under-bar"
             :class="{'active-menu-container-under-bar': menu === 'game'}">
          <img class="icon-under-bar" src='@/assets/game-icon.png'>

        </div>
        <ul v-if="menu === 'game'" @click="clickRouter"
            class="sub-menu-under-bar">
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
      <li class="user-info-container-under-bar" @click="clickUserInfo">
        <img class="user-profile-img" :src="user.userProfileImg || defaultImage">
      </li>
    </ul>
  </nav>
</template>


<script setup>
import {getCurrentInstance, onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from '@/assets/img.png';

const router = useRouter();
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})
const isLoggedIn = ref(false);

const menuItems = ['home', 'playground', 'team', 'game']; // 메뉴 항목들
const menuCount = menuItems.length;


onMounted(async () => {
  await getUserInfo();
});

const clickRouter = async () => {
  menu.value = "";
}

const menu = ref("");

const toggleMenu = (selectedMenu, event) => {
  if (menu.value === selectedMenu && selectedMenu !== 'home') {
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

<style>
.logo-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
}

.logo-mobile {
  display: flex;
  color: #4c8ba8;
  font-family: MiSans-Medium,sans-serif;
  justify-content: center;
  align-content: center;
}

.logo-mobile img {
  width: 20px;
  height: 20px;
  margin: auto 5px;
}

.navigation-under-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-around;
  background-color: #fff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.nav-links-under-bar {
  display: flex; /* 항목들을 가로로 배치 */
  padding: 0;
  margin: 0;
  width: 100%; /* 전체 너비를 사용 */
}

.nav-links-under-bar li {
  flex: 1; /* 모든 항목이 동일한 공간을 차지하도록 함 */
  text-align: center; /* 텍스트를 중앙으로 정렬 */
}

.menu-container-under-bar {
  display: flex;
  justify-content: center;
  opacity: 0.6;
  padding: 20px;
  border-radius: 8px;
}

.sub-nav-item {
  z-index: 2000;
}

.active-menu-container-under-bar .icon-under-bar {
  opacity: 1;

}

.icon-under-bar {
  width: 30px;
  height: 30px;
  display: block;
  opacity: 0.4;
}

.user-info-container-under-bar {
  display: flex;
  justify-content: center;
  margin: auto 0;
}

.user-info-container-under-bar img {
  width: 30px;
  height: 30px;
  background-color: #eee;
  border-radius: 50%;
}

.sub-menu-under-bar {
  position: fixed; /* absolute에서 fixed로 변경 */
  bottom: 60px; /* navigation-under-bar의 높이에 맞게 조정 */
  width: 100%;
  justify-content: center;
  left: 0; /* 화면 왼쪽에서 시작 */
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  padding: 2px 10px;
  z-index: 2000;
  display: none; /* 기본적으로 보이지 않도록 설정 */
  transition: all 0.3s ease;

}

.sub-menu-under-bar li {
  margin: 0 0px 5px 0px;
  list-style-type: none;
  width: 100%;
  padding: 5px;
  font-size: 13px;
  letter-spacing: 2px;
  border-bottom: 1px solid #b9b9b9;
}

.sub-menu-under-bar li:hover {
  color: black;
}

.sub-menu-under-bar {
  display: block;
}

.router-link-active {
  font-family: MiSans-Semibold, sans-serif;
  color: black;
}


@media (max-width: 600px) {
  .logo-bar {
    width: 100%;
    margin: 0;
    padding: 10px;
  }


}

</style>