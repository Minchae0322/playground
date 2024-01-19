<template>

  <input type="text" v-model="searchQuery" placeholder="팀 이름 검색..." />

  <div class="team-container">
    <div v-for="team in filteredTeams" :key="team.teamId" class="team-card">
      <img :src=team.teamProfileImg class="team-img" :alt=defaultImage>
      <div class="team-name">{{ team.teamName }}</div>
      <div class="team-info-container">
      <div>{{ team.teamDescription }}</div>
        <div>{{team.sportsEvent}}</div>
      </div>
      <router-link :to="{name:'teamInfo', params:{teamId: team.teamId}}">
        <button>View Details</button>
      </router-link>

    </div>
  </div>

</template>


<script setup>
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";

const searchQuery = ref(''); // 검색어를 위한 반응형 데이터
const router = useRouter();
const apiBaseUrl = "http://localhost:8080";

const teams = ref([{
  teamId: 1,
}]);

onMounted(async () => {
  await getTeams('school');
});

const getTeams = async (type) => {
  await validateAccessToken()
  
  try {
    const response = await axios.post(`${apiBaseUrl}/team/list/${type}`, {

    },{
      headers: {
        'Authorization': getAccessToken(),
      }
    },)
    teams.value = response.data.map(team => ({
      ...team,
      teamProfileImg: team.teamProfileImg ? `data:image/jpeg;base64,${team.teamProfileImg}` : defaultImage,
    }));
  } catch (error) {

  }
};

const filteredTeams = computed(() => {
  return teams.value.filter(team =>
      team.teamName && team.teamName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

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

.team-container {
  display: flex;
  justify-content: space-around;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
}

.team-card {
  width: 20%; /* 카드의 폭 */
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  border-radius: 5px; /* 모서리 둥글게 */
  padding: 20px;
  background-color: #fff;
}

.team-img {
  width: 100%;
  height:100%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 15; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  display: block; /* 이미지를 블록 요소로 만들어 불필요한 여백 제거 */
}

.team-name {
  margin: 10px 0;
  text-align: start;
  width: 100%;
}

.team-info-container {
  margin: 10px 0;
  text-align: start;
  width: 100%;
}

.team-card button {
  background-color: #000;
  color: #fff;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
}

.team-card button:hover {
  background-color: #555;
}
</style>