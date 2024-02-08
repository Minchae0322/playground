<template>
  <head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title></title>

  </head>
  <div class="info-container">
    <h2>队伍</h2>
    <p>Check out our playground</p>
    <div class="info-container-border"></div>
  </div>
  <div class="sports-event-container">
    <button @click="getTeams('sportsEvent','Soccer')">足球</button>
    <button @click="getTeams('sportsEvent','Basketball')">篮球</button>
    <button @click="getTeams('sportsEvent','Tennis')">网球</button>
    <button @click="getTeams('sportsEvent','Table_tennis')">乒乓球</button>
    <button @click="getTeams('sportsEvent','Badminton')">羽毛球</button>
  </div>

  <input type="text" v-model="searchQuery" placeholder="..."/>
  <div class="team-width">
    <div class="team-container">
      <div v-for="team in filteredTeams" :key="team.teamId" class="team-card">
        <router-link :to="{name:'teamInfo', params:{teamId: team.teamId}}">
          <img :src=team.teamProfileImg class="team-img" :alt=defaultImage>
          <div class="team-name">{{ team.teamName }}</div>
          <div class="team-info-container">
            <div>{{ team.sportsEvent }}</div>
          </div>
        </router-link>
      </div>

    </div>
  </div>
</template>


<script setup>
import {computed, getCurrentInstance, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";
import Vibrant from 'node-vibrant'

const searchQuery = ref(''); // 검색어를 위한 반응형 데이터
const router = useRouter();
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

const teams = ref([{
  teamId: 1,
  vibrantColor: '',
}]);

onMounted(async () => {
  await getTeams('school', '');
});

const getTeams = async (type, sportsEvent) => {
  await validateAccessToken()

  try {
    const response = await axios.post(`${apiBaseUrl}/team/list/${type}`, {
      sportsEvent: sportsEvent,
    }, {
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

const getTeamsBySportsEvent = async () => {
  await validateAccessToken()

  try {
    const response = await axios.post(`${apiBaseUrl}/team/list/${type}`, {}, {
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


const extractColors = (team) => {
  if (team.teamProfileImg) {
    Vibrant.from(team.teamProfileImg)
        .getPalette()
        .then((palette) => {
          team.vibrantColor = palette.Vibrant
        })
        .catch((error) => {
          console.error('Error extracting vibrant color:', error);
        });
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
      headers: {'RefreshToken': refreshToken}
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

a {
  text-decoration: none;
  color: var(--text-hint-dark);
}

.info-container {
  margin: 10px 30px;
}

.info-container h2 {
  font-size: 1.8rem;
  color: #333;
  text-align: start;
  font-family: MiSans-Heavy, sans-serif;
}

.info-container-border {
  margin-right: 100px;
  margin-left: 200px;
  width: 70%;
  border-bottom: 1px solid var(--text-hint);
}

.info-container p {
  font-size: 0.8rem;
  color: #666;
  margin-right: auto;
  margin-left: 100px;
  margin-bottom: 20px;
  font-family: MiSans-Normal, sans-serif;
}

input[type="text"] {
  width: 95%; /* 전체 너비를 사용 */
  padding: 10px 15px; /* 내부 여백 */
  margin: 10px 10px; /* 외부 여백 */
  box-sizing: border-box; /* 패딩과 테두리가 너비에 포함되도록 설정 */
  border: 1px solid #ccc; /* 테두리 설정 */
  border-radius: 5px; /* 모서리를 둥글게 */
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1); /* 내부 그림자 */
  outline: none; /* 포커스 시 기본 윤곽선 제거 */
  transition: border-color 0.3s ease-in-out; /* 부드러운 테두리 색상 변화 */
}

input[type="text"]:focus {
  border-color: #7FC7D9; /* 포커스 시 테두리 색상 변경 */
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.2), 0 0 8px rgba(74, 144, 226, 0.5); /* 포커스 시 그림자 강조 */
}

input[type="text"]::placeholder {
  color: #888; /* 플레이스홀더 텍스트 색상 */
  font-style: italic; /* 플레이스홀더 텍스트 스타일 */
}

.sports-event-container {
  display: flex;
  margin-left: 20px;
}

.sports-event-container button {
  text-align: center;
  width: 80px;
  height: 30px;
  margin: 5px 5px;
  background-color: var(--text-primary);
  color: white;
  border: none;
  border-radius: 4px;
  font-family: MiSans-Semibold, sans-serif;
}

.sports-event-container button:hover {

  background-color: #365486;

  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease; /* 부드러운 전환 효과 */
}

.team-width {
  display: flex;
  width: 100%;
  min-width: 1150px;
  justify-content: center;
}

.team-container {
  display: flex;
  width: 95%;
  min-width: 1050px;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  padding: 10px;
}

.team-card {
  position: relative;
  width: 24%; /* 카드의 폭 */
  text-align: start;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  border-radius: 8px; /* 모서리 둥글게 */
  padding: 10px 20px;
  border-bottom: 5px solid var(--primary-color);
  background-color: #fff;
}

.team-card:hover {
  background-color: #DCF2F1; /* 배경색 변경 */
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2); /* 그림자 효과 강조 */
  transform: translateY(-3px); /* 조금 위로 움직임 */
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease; /* 부드러운 전환 효과 */
}

.team-card::after {

  content: '\f054'; /* Font Awesome 오른쪽 화살표 아이콘 */
  font-family: 'Font Awesome 5 Free', serif; /* Font Awesome 폰트 설정 */
  font-weight: 900; /* Font Awesome 아이콘을 위한 폰트 두께 */
  position: absolute; /* 절대 위치 설정 */
  right: 10px; /* 오른쪽에서 10px 떨어진 곳에 위치 */
  bottom: 10px; /* 아래에서 10px 떨어진 곳에 위치 */
  font-size: 1.5rem; /* 아이콘 크기 */
  color: #0F1035; /* 아이콘 색상 */
}

.team-img {
  width: 40%;
  border: 1px solid #d9d9d9;
  height: 40%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 17 / 18; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-radius: 8px;
  margin-bottom: 20px;
  display: block; /* 이미지를 블록 요소로 만들어 불필요한 여백 제거 */
}

.team-name {
  text-align: start;
  font-size: 1.3em;
  color: var(--text-black);
  letter-spacing: 1px;
  font-family: MiSans-Heavy, sans-serif;
  width: 100%;
}

.team-info-container {
  text-align: start;
  width: 100%;

  font-family: MiSans-Normal, sans-serif;

}

</style>