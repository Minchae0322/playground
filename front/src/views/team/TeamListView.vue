<template>
  <div class="team-container">
    <div class="team-card">
      <div class="team-logo"><!-- 로고 이미지 들어갈 위치 --></div>
      <h3>Team Thunder</h3>
      <p>A team of innovative thinkers and problem solvers.</p>
      <button>View Details</button>
    </div>
    <div class="team-card">
      <div class="team-logo"><!-- 로고 이미지 들어갈 위치 --></div>
      <h3>Team Lightning</h3>
      <p>A team of fast-paced, agile developers.</p>
      <button>View Details</button>
    </div>
    <div class="team-card">
      <div class="team-logo"><!-- 로고 이미지 들어갈 위치 --></div>
      <h3>Team Tornado</h3>
      <p>A team of creative designers and artists.</p>
      <button>View Details</button>
    </div>
    <div class="team-card">
      <div class="team-logo"><!-- 로고 이미지 들어갈 위치 --></div>
      <h3>Team Cyclone</h3>
      <p>A team of strategic planners and analysts.</p>
      <button>View Details</button>
    </div>
  </div>

</template>


<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";


const router = useRouter();
const apiBaseUrl = "http://localhost:8080";

const teams = ref([{

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
  width: 240px; /* 카드의 폭 */
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  border-radius: 5px; /* 모서리 둥글게 */
  padding: 20px;
  background-color: #fff;
}

.team-logo {
  height: 150px; /* 로고 영역 높이 */
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
}

.team-card h3 {
  margin: 10px 0;
}

.team-card p {
  margin: 10px 0 20px;
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