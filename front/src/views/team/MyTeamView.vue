<template>
  <div class="teams-myTeam">
    <div class="teams-title-myTeam">
      <h3>我的队伍</h3>
    </div>
    <div v-for="team in teams" :key="team.teamId" class="teams-item-myTeam">
      <router-link class="team-container-myTeam" :to="{ name:'teamInfo', params: { teamId: team.teamId } }">
        <div class="border"></div>
        <img :src="team.teamProfileImg || defaultImage" class="team-image-myTeam"/>
        <div class="team-info-container-myTeam">
          <div class="team-name-myTeam">{{ team.teamName }}</div>
          <div class="team-sportsEvent-myTeam">{{ team.teamSportsEvent }}</div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup>

import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from "@/assets/img.png";
import {getCurrentInstance, onMounted, ref} from "vue";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
const teams = ref([{
  teamId: '1',
  teamName: '',
  teamProfileImg: ref(''),
  teamSportsEvent: '',

}]);

onMounted(async () => {
  await getTeams()
});

const getTeams = async () => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/user/teams`, {
      headers: {
        'Authorization': localStorage.getItem("accessToken")
      }
    });
    teams.value = response.data.map(team => ({
      teamId: team.teamId,
      teamName: team.teamName,
      teamSportsEvent: team.sportsEvent,
      teamProfileImg: team.teamProfileImg ? `data:image/jpeg;base64,${team.teamProfileImg}` : defaultImage,
    }));
  } catch (error) {
    console.error('팀 정보를 가져오는데 실패했습니다.', error.response.data.message);
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

<style>
.teams-myTeam {
  padding: 20px;
  margin: 0 auto;
  width: 90%;
  line-height: 1.2;
  min-width: 1045px;
  background: var(--background-color-gray); /* 배경색을 추가할 수도 있습니다 */
}
.router-link h2 {
  text-decoration: none;
}

.team-container-myTeam {
  display: flex;
  height: 100%;
  justify-content: start;
  align-items: center;
}

.team-container-myTeam img {
  width: 50px; /* 적절한 크기 설정 */
  height: 50px; /* 적절한 크기 설정 */
  border-radius: 25%;
  margin-right: 20px;
}

.team-info-container-myTeam {
  display: flex;
  flex-direction: column;
  text-align: start;
}

button {
  padding: 10px;
  cursor: pointer;
}

.teams-myTeam h3 {
  display: flex;
  margin-left: 10px;
  margin-top: 10px;
  text-align: start;
  align-items: center;
}

.teams-myTeam ul {
  list-style: none;
  padding: 0;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  flex-wrap: wrap;
}

.teams-item-myTeam {
  background-color: #ffffff;
  padding: 10px;
  margin: 5px;
  border-radius: 5px;
  text-align: center;
  flex-basis: calc(33.333% - 10px); /* 3개의 아이템을 한 줄에 나타내고 싶을 때 */
}
.teams-item-myTeam:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.2); /* 그림자 효과 강조 */
  transform: translateY(-3px); /* 조금 위로 움직임 */
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease; /* 부드러운 전환 효과 */
}


.team-name-myTeam {
  font-family: MiSans-Heavy,sans-serif;
  letter-spacing: 1px;
  font-size: 18px; /* 적절한 크기 설정 */
  color: var(--text-primary);

}

.teams-title-myTeam {
  display: flex;
  align-items: center;
  border-radius: 8px 8px 0 0;
  background: var(--primary-gradient);
  color: white;
  padding: 0 5px 10px;
}
.team-sportsEvent-myTeam {
  font-size: 14px; /* 적절한 크기 설정 */
  color: var(--text-hint-dark);
  font-family: MiSans-Light,sans-serif;
}

@media (max-width: 600px) {
  .teams-myTeam {
    min-width: 400px;
    width: 95%;
  }
}


</style>