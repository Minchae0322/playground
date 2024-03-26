<template>
  <head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title></title>
  </head>

  <div class="page-title-container">
    <h2>队伍</h2>
    <p>Check out our playground</p>
    <div class="page-title-container-border"></div>
  </div>

  <div class="sports-event-container-teamList">
    <button @click="getTeams('sportsEvent','Soccer')">足球</button>
    <button @click="getTeams('sportsEvent','Basketball')">篮球</button>
    <button @click="getTeams('sportsEvent','Tennis')">网球</button>
    <button @click="getTeams('sportsEvent','Table_tennis')">乒乓球</button>
    <button @click="getTeams('sportsEvent','Badminton')">羽毛球</button>
  </div>

  <input class="search-teamList" type="text" v-model="searchQuery" placeholder="..."/>

    <div class="team-list-container-teamList">
      <div class="team-card-teamList" v-for="team in filteredTeams" :key="team.teamId" >
        <router-link :to="{name:'teamInfo', params:{teamId: team.teamId}}">
          <img :src="team.teamProfileImg" class="team-card-img-teamList">
          <div class="team-list-teamName">{{ team.teamName }}</div>
          <div class="team-list-info-container">
            <div>{{ team.sportsEvent }} </div>
            <div class="more-teamList"></div>
          </div>
        </router-link>
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
      teamProfileImg: '../team' +  team.teamProfileImg
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
      teamProfileImg: team.teamProfileImg
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


<style>
.sports-event-container-teamList {
  display: flex;
  margin-left: 20px;
}

.sports-event-container-teamList button {
  text-align: center;
  width: 70px;

  margin: 5px 5px;
  background-color: var(--text-primary);
  color: white;
  border: none;
  border-radius: 4px;
  font-family: MiSans-Semibold, sans-serif;
}

.sports-event-container-teamList button:hover {
  background-color: #365486;
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease; /* 부드러운 전환 효과 */
}


.search-teamList {
  display: flex;
  width: 95%; /* 전체 너비를 사용 */
  padding: 10px 15px; /* 내부 여백 */
  margin: 0 auto;
  box-sizing: border-box; /* 패딩과 테두리가 너비에 포함되도록 설정 */
  border: 1px solid #ccc; /* 테두리 설정 */
  border-radius: 5px; /* 모서리를 둥글게 */
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1); /* 내부 그림자 */
  outline: none; /* 포커스 시 기본 윤곽선 제거 */
  transition: border-color 0.3s ease-in-out; /* 부드러운 테두리 색상 변화 */
}

.search-teamList:focus {
  border-color: #7FC7D9; /* 포커스 시 테두리 색상 변경 */
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.2), 0 0 8px rgba(74, 144, 226, 0.5); /* 포커스 시 그림자 강조 */
}

.search-teamList::placeholder {
  color: #888; /* 플레이스홀더 텍스트 색상 */
  font-style: italic; /* 플레이스홀더 텍스트 스타일 */
}

.team-list-container-teamList {
  display: flex;
  width: 95%;

  min-width: 1045px;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
  margin: 0 auto;
  gap: 10px;
  padding: 10px;
}


.team-card-teamList {
  width: 24%; /* 카드의 폭 */
  text-align: start;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  border-radius: 8px; /* 모서리 둥글게 */
  padding: 10px 20px;
  border-bottom: 5px solid var(--primary-color);
  background-color: #fff;
}


.team-card-teamList:hover {
  background-color: #DCF2F1; /* 배경색 변경 */
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2); /* 그림자 효과 강조 */
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease; /* 부드러운 전환 효과 */
}

.more-teamList::after {
  content: '\f054'; /* Font Awesome 오른쪽 화살표 아이콘 */
  font-family: 'Font Awesome 5 Free', serif; /* Font Awesome 폰트 설정 */
  font-weight: 900; /* Font Awesome 아이콘을 위한 폰트 두께 */
  font-size: 1rem; /* 아이콘 크기 */
  color: #0F1035; /* 아이콘 색상 */
}

.team-card-img-teamList {
  width: 40%;
  border: 1px solid #d9d9d9;
  height: 40%; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 17 / 18; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-radius: 8px;
  margin-bottom: 20px;
  display: block; /* 이미지를 블록 요소로 만들어 불필요한 여백 제거 */
}

.team-list-teamName {
  text-align: start;
  font-size: 1.3em;
  color: var(--text-black);
  letter-spacing: 1px;
  font-family: MiSans-Heavy, sans-serif;
  width: 100%;
}

.team-list-info-container {
  text-align: start;
  width: 100%;
  display: flex;
  justify-content: space-between;
  font-family: MiSans-Normal, sans-serif;

}

@media (max-width: 600px) {
  .team-list-container-teamList {
    min-width: 400px;
    width: 100%;

    margin-bottom: 50px;
  }

  .team-card-teamList {
    width: 47%;
    margin: 0 auto;
  }

  .sports-event-container-teamList {
    width: 95%;
    margin: 0 auto;
  }

  .sports-event-container-teamList button {
    width: 65px;
    font-size: 10px;
  }
  .search-teamList {
    width: 95%;

  }
}

</style>