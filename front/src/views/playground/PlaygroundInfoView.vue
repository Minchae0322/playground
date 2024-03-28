<template>
  <div id="gameView-container">
    <div class="upcoming-games">
      <h3>未开始比赛</h3>
      <ul>
        <li v-for="(game,gameId) in upcomingGames" :key="gameId" @click="handleGameClick(game)">
          <div v-if="game.gameId">
            <div class="game-card">
              <div class="game-type-container">
                <img v-if="game.gameType === '竞争'" :src="rankingIcon">
                <img v-else :src="friendlyIcon">
                <p class="game-type">{{ game.gameType }}</p>
              </div>
              <div class="game-info">
                <div class="game-name">{{ game.gameName }}</div>
                <div class="game-time">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                       stroke-width="3" stroke-linecap="round" stroke-linejoin="round"
                       class="time-icon">
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="12 6 12 12 16 14"></polyline>
                  </svg>
                  {{ game.gameStart }}（ {{ game.runningTime }}分 ）
                </div>
                <div class="host-info">
                  <img :src="game.hostProfileImg || defaultImage">
                  <div class="host-name">{{ game.hostName }}</div>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>


  </div>

</template>

<script setup>
import {getCurrentInstance, inject, onMounted, ref} from 'vue';
import axios from "axios";
import {useRouter} from "vue-router";
import {defineEmits} from 'vue';
import friendlyIcon from '../../assets/icon-handshake.png'
import rankingIcon from '../../assets/icon-ranking.png'


const upcomingGames = ref([{}]);
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
const emits = defineEmits(['gameSelected']);
const props = defineProps({
  playgroundId: Number,
})
const frontBaseUrl = inject('frontBaseUrl');
const getImageUrl = (file) => {
  return frontBaseUrl + file;
};

onMounted(async () => {
  // Check if the initial page number is provided in the route query

  await getUpcomingGames();
});

function handleGameClick(game) {
  // 게임이 클릭되었을 때 부모 컴포넌트에 알림
  emits('gameSelected', game);
}

const getUpcomingGames = async () => {
  await validateAccessToken()
  await axios.get(`${apiBaseUrl}/playground/${props.playgroundId}/upComing`,
      {
        headers: {
          'Authorization': getAccessToken()
        }
      }
  ).then(response => {
    upcomingGames.value = response.data.map(game => ({
      ...game,
      hostProfileImg: game.hostProfileImg ? `data:image/jpeg;base64,${game.hostProfileImg}` : defaultImage,
      onClick: () => showGameInfo(game.gameId)
    }));
  });

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
.game-image img {
  width: 100%;
  height: auto; /* 높이를 자동으로 설정하여 원본 이미지 비율 유지 */
  aspect-ratio: 18 / 7; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.upcoming-games {
  line-height: 1.2;
  padding: 15px;
  min-height: 90vh;
  background-color: #fff; /* 배경색을 흰색으로 설정 */
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.upcoming-games h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 1.25rem; /* 크기를 조정하여 가독성을 높임 */
}

.upcoming-games ul {
  list-style: none;
  padding: 0;
}

.upcoming-games li {
  margin-bottom: 15px; /* 각 게임 카드 사이의 간격을 늘림 */
  color: #555;
}




.game-info {
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: end;
  flex-basis: 67%;
  margin: auto 0 auto auto;
}

.game-card {
  display: flex;
  border: 1px solid #ddd;
  padding: 15px 20px;
  font-size: 0.875rem;
  color: #666;
  margin-top: 0.5rem;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05); /* 그림자를 더 부드럽게 조정 */
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.game-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 강조 */
}


.game-name {
  font-size: 19px;
  font-family: MiSans-Semibold, sans-serif;
}

.time-icon {
  margin: auto 7px auto 0;
  height: 15px;
}

.game-time {
  display: flex;
  font-size: 13px;
  align-content: center;
  color: var(--text-hint-dark);
  margin-top: 7px;
  font-family: MiSans-Medium, sans-serif;
}

.host-info {
  margin-top: 10px;
  display: flex; /* Flexbox를 이용한 가로 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  margin-bottom: 0.5rem; /* 아래쪽 여백 */
}

.host-name {
  font-family: MiSans-Medium, sans-serif;
}

/* 아이콘 스타일링 */
.host-info img {
  border-radius: 50%;
  background-color: #ddd; /* 아이콘 배경색 설정 */
  display: inline-block;
  width: 35px; /* 아이콘 크기 조정 */
  height: 35px;

  margin-right: 8px; /* 아이콘과 텍스트 사이의 간격 조정 */
  vertical-align: middle; /* 수직 정렬 */
}

/* 시간과 실행 시간 정보 스타일링 */

.game-type {
  font-size: 0.875rem;
  color: #666;
  text-align: center;
  align-content: center;
  justify-content: center;

}

/* 링크 스타일링 제거 */
.game-card a {
  text-decoration: none;
  color: inherit;
}

a {

  text-decoration: none;
}

.game-type-container {
  display: flex;
  flex-basis: 20%;
  flex-direction: column;
}

.game-type-container img {
  background: none;
  width: 80px; /* 아이콘 크기 */
  height: 80px;
  border-radius: 4px;
  margin: 0 auto;
}




@media (max-width: 600px) {
  .upcoming-games {
    min-height: 0;
  }

}


</style>