<template>
  <div id="gameResultModal" class="modal-overlay">

      <div class="result-details-container">
        <div class="game-info-result">
          <div class="game-title-result">{{ props.gameName }}
            <div class="game-type-result">{{ props.gameType }}</div>
          </div>

          <div class="game-date-result">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="1" stroke-linecap="round" stroke-linejoin="round"
                 class="time-icon">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            {{ formatDate(props.gameStart) }}
            <div class="game-location-result"><img src="../../assets/icon-location.png">{{ props.location }}</div>
          </div>

        </div>
        <div class="modal-gameResult">
          <div class="score-gameResult">
            <div class="score-group">
              <label for="homeTeamScore">Home</label>
              <input type="number"
                     id="homeTeamScore"
                     name="homeTeamScore"
                     min="0"
                     class="input-field"
                     v-model="homeScore"
                     @click="increaseScore('home')"
                     readonly>
              <div class="add-score-button-container">
                <button class="add-score-button" type="button" @click="addScore('home', 1)">加1分</button>
                <button class="add-score-button" type="button" @click="addScore('home', 10)">加10分</button>
              </div>
            </div>

           <img class="refresh-icon" @click=refreshScore src="@/assets/icons-refresh.png">


            <div class="score-group">
              <label for="awayTeamScore">Away</label>
              <input type="number"
                     id="homeTeamScore"
                     name="homeTeamScore"
                     min="0"
                     class="input-field"
                     v-model="awayScore"
                     @click="increaseScore('away')"
                     readonly>
              <div class="add-score-button-container">
                <button class="add-score-button" type="button" @click="addScore('away', 1)">加1分</button>
                <button class="add-score-button" type="button" @click="addScore('away', 10)">加10分</button>
              </div>
            </div>
          </div>


        </div>
        <div class="button-group">
          <button type="button" @click="close" class="cancel-button">取消</button>
          <button type="submit" @click="summitResult" class="save-button">保存</button>
        </div>
      </div>
    </div>

</template>

<script setup>
import {defineEmits, defineProps, getCurrentInstance, ref} from "vue";
import axios from "axios";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const emit = defineEmits(['close',]);
const props = defineProps({
  someData: String,
  gameId: Number,
  gameName: String,
  gameStart: String,
  gameType: String,
  location: String,
});
const homeScore = ref(0);
const awayScore = ref(0);

const increaseScore = async (homeAndAway) => {
  if (homeAndAway === "home") {
    homeScore.value++;
  }
  if (homeAndAway === "away") {
    awayScore.value++;
  }
}

const addScore = async (homeAndAway, score) => {
  if (homeAndAway === "home") {
    homeScore.value += score;
  }
  if (homeAndAway === "away") {
    awayScore.value += score;
  }
};

const close = () => {
  emit('closeResultModal');
};

function formatDate(dateTime) {
  const date = new Date(dateTime);
  return date.toLocaleDateString();
}

const summitResult = async () => {
  await validateAccessToken()

  try {
    await axios.post(`${apiBaseUrl}/game/result/${props.gameId}/${props.gameType}`, {
      gameId: props.gameId,
      homeScore: homeScore.value,
      awayScore: awayScore.value,
    }, {
      headers: {'Authorization': getAccessToken()},
    });
  } catch (error) {
    await updateAccessToken();
  }
};

const refreshScore = async () => {
  homeScore.value = 0;
  awayScore.value = 0;
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
.modal-overlay {
  position: fixed;
  top: 0;
  padding: 1px;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.result-details-container {
  min-width: 550px;
  margin: auto;
  width: 40%; /* 너비를 50%로 설정 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 5px 1px 8px 0 rgba(0, 0, 0, .06);
  border-left: 1px solid rgba(0, 0, 0, .08);;
  background: #fff; /* 카드의 배경색 */
  overflow: auto; /* 내용이 넘칠 때 스크롤바를 보여줌 */

}

.game-info-result {
  display: flex;
  padding: 5px;
  flex-direction: column;
}

.game-title-result {
  font-size: 28px;
  font-family: MiSans-Semibold, sans-serif;
  color: black;
  display: flex;
}

.game-date-result {
  display: flex;
  font-size: 18px;

  color: var(--text-hint-dark);
  font-family: MiSans-Medium, sans-serif;
}

.game-date-result svg {
  width: 14px;
  color: black;
  margin-right: 5px;
}

.game-type-result {
  color: #eab800;
  font-size: 18px;
  margin: auto 10px;
}


.game-location-result {
  font-size: 16px;
  display: flex;
  margin-left: 5px;
  color: #0064cc;
  font-family: MiSans-Medium, sans-serif;
}

.game-location-result img {
  width: 15px;
  height: 15px;
  margin: auto 2px auto 0;

}

.score-group {
  margin: 0 auto;
}

.score-group label {
  display: block;

  color: rgba(51, 51, 51, 0.6);
  font-size: 1.4rem;
}

.refresh-icon {
  width: 40px;
  height: 40px;
}

.refresh-icon:hover {
  opacity: 0.7;
}

.input-field {
  width: 150px;
  height: 150px;
  border: 2px solid black;
  border-radius: 4px;
  text-align: center;
  font-size: 32px;
  line-height: 100px;
}

/* 선택 가능한 입력 필드의 외형을 제거 */
.input-field::-webkit-inner-spin-button,
.input-field::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.modal-gameResult {
  display: flex;
  flex-direction: column;
  margin: 10px;

}

.score-gameResult {
  display: flex;
  justify-content: center; /* 가로축 중앙 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  flex-direction: row; /* 항목들을 가로로 나열 */
  width: 100%;
}

.add-score-button-container {
  display: flex;
  margin-top: 5px;
  gap: 10px;
}

.add-score-button {
  background-color: white;
  color: black;
 border: 2px solid black;
  letter-spacing: 2px;
  font-family: MiSans-Semibold,sans-serif;
  border-radius: 4px;
}

.button-group {
  text-align: right; /* 버튼을 오른쪽으로 정렬 */
  margin-top: 30px;
}

.cancel-button {
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  margin-left: 10px;
  cursor: pointer;
  background-color: red;
}

.save-button {
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  margin-left: 10px;
  cursor: pointer;
  background-color: black;
}


</style>