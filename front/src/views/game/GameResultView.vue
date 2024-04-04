<template>
  <div id="gameResultModal" class="modal-overlay">
    <div class="modal-content">

      <h2 class="modal-header">{{ props.gameName}}</h2>
      <h2 class="modal-header">{{ props.gameStart}}</h2>
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
            <div>
            <button type="button" onclick="addScore('homeTeamScore', 1)">1점 추가</button>
            <button type="button" onclick="addScore('homeTeamScore', 10)">10점 추가</button>
            </div>
          </div>

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
            <div>
            <button type="button" onclick="addScore('awayTeamScore', 1)">1점 추가</button>
            <button type="button" onclick="addScore('awayTeamScore', 10)">10점 추가</button>
            </div>
          </div>
        </div>
        <div class="button-group">
          <button type="button" @click="close" class="cancel-button">取消</button>
          <button type="submit" @click="summitResult" class="save-button">保存</button>
        </div>
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

const close = () => {
  emit('closeResultModal');
};


const summitResult = async () => {
  await validateAccessToken()

  try {
    await axios.post(`${apiBaseUrl}/game/result/${props.gameId}/${props.gameType}`, {
      gameId: props.gameId,
      homeScore: homeScore.value,
      awayScore: awayScore.value,
    },{
      headers: {'Authorization': getAccessToken()},
    });
  } catch (error) {
    await updateAccessToken();
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

.score-group {
  margin: 0 auto;


}

.score-group label {
  display: block;
  margin-bottom: .5rem;
  color: #333;
  font-size: 1.2rem;
}


.input-field {
  width: 200px;
  height: 200px;
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
}

.score-gameResult {
  display: flex;
  justify-content: center; /* 가로축 중앙 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  flex-direction: row; /* 항목들을 가로로 나열 */
  width: 100%;
}

.modal-content {
  background: white;
  margin: 10% auto;
  padding: 2rem;
  border: 1px solid #ccc;
  width: 50%;
  border-radius: 10px;
}

.modal-header {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  text-align: center;
}



.button-group {
  text-align: right; /* 버튼을 오른쪽으로 정렬 */
}

.cancel-button,
.save-button {
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  margin-left: 10px;
  cursor: pointer;
}


</style>