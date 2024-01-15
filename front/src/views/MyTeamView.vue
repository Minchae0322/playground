<template>
  <div class="teams">
    <div class="teams-title">
      <h3>Teams</h3>
    </div>
    <div v-for="team in teams" :key="team.teamId" class="team-item">
      <router-link class="team-container" :to="{ name:'teamInfo', params: { teamId: team.teamId } }">
        <img :src="team.teamProfileImg || defaultImage" class="team-image"/>
        <div class="teams-info">
          <h2 class=" team-name">{{ team.teamName }}</h2>
          <h2 class="team-sportsEvent">종목: {{ team.teamSportsEvent }}</h2>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup>

import {useRouter} from "vue-router";
import axios from "axios";
import defaultImage from "@/assets/img.png";
import {onMounted, ref} from "vue";

const apiBaseUrl = "http://localhost:8080";
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

<style scoped>
.teams {
  height: 100%;
  background: #f9fbfc; /* 배경색을 추가할 수도 있습니다 */
}
.router-link h2 {
  font-family: gothic-bold;
  text-decoration: none;
}

.team-container {
  display: flex;
  justify-content: start;
  align-items: center;
}

.team-container img {
  width: 50px; /* 적절한 크기 설정 */
  height: 50px; /* 적절한 크기 설정 */
  border-radius: 25%;
  margin-right: 20px;
}

.teams-info {
  display: flex;
  flex-direction: column;
  text-align: start;
}

button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  background-color: #f5f5f5;
  cursor: pointer;
}

button:hover {
  background-color: #e1e1e1;
}

.teams h3 {
  display: flex;
  margin-left: 10px;
  margin-top: 10px;
  text-align: start;
  align-items: center;
}

.teams ul {
  list-style: none;
  padding: 0;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  flex-wrap: wrap;
}

.team-item {
  background-color: #ffffff;
  padding: 10px;
  margin: 5px;
  border-radius: 5px;
  text-align: center;

  flex-basis: calc(33.333% - 10px); /* 3개의 아이템을 한 줄에 나타내고 싶을 때 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}



.team-name {
  font-size: 14px; /* 적절한 크기 설정 */
  color: #333;

}

.teams-title {
  display: flex;
  align-items: center;
  border-radius: 12px 12px 0 0;
  background: var(--primary-gradient);

  color: white;
  padding: 0 5px 10px;
}
.team-sportsEvent {
  font-size: 14px; /* 적절한 크기 설정 */
}
/* 추가적으로 반응형 디자인을 고려할 수 있습니다. */
a {
  text-decoration: none;
  font-family: gothic-bold;
}


</style>