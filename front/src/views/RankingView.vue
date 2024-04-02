<template>

  <div class="page-title-container">
    <h2>排行帮</h2>
    <p>Check out our playground</p>
    <div class="page-title-container-border"></div>
  </div>

  <div class="ranking-table-container">
    <table class="ranking-table">
      <thead>
      <tr>
        <th>排位</th>
        <th>头像</th>
        <th>用户名</th>
        <th>胜</th>
        <th>败</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in userRankings" :key="user.id" >
        <td class="rank">
          <span class="rank-number">{{user.ranking }}</span>

        </td>
        <td class="profile-picture-cell">
          <img :src="getImageUrl(user.userProfileImg)" class="profile-picture">
        </td>
        <td>{{ user.userNickname }}</td>
        <td :class="{'win-color': true}">{{ user.win }}W</td>
        <td :class="{'lose-color': true}">{{ user.lose }}L</td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>
import {ref, onMounted, inject, getCurrentInstance} from 'vue';
import axios from "axios";


const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const userRankings = ref([]);
const frontBaseUrl = inject('frontBaseUrl');
const getImageUrl = (file) => {
  return frontBaseUrl + file;
};


onMounted(async () => {
  await getUserRanking();
});

const getUserRanking = async () => {
  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/user/ranking`, {
      headers: {'Authorization': getAccessToken()},
    });
    userRankings.value = response.data.map(ranking => ({
      ...ranking
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
.ranking-table-container {
  width: 90%;
  margin: 0 auto;

}



.ranking-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  text-align: center;

}


.ranking-table thead {
  background-color: var(--secondary-color);
}

.ranking-table thead th {
  font-family: MiSans-Semibold,sans-serif;
  color: white;

  padding: 15px 10px;
}

.ranking-table tbody tr {

  border-bottom: 1px solid #ddd;
}

.ranking-table td {
  padding: 10px;
  border-radius: 10%;
}

.rank-number {
  font-weight: bold;
  font-size: 24px;
  color: #FFD700;
  position: relative;
}

.rank-icon {
  /* 사용자 정의 아이콘 스타일링 추가 */
}

.profile-picture-cell {
  width: 50px; /* 조정 가능 */
}

.profile-picture {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.new-entry .rank-number {

  padding-left: 30px; /* 아이콘 크기에 맞게 조정 */
}
.win-color {
  color: green; /* 승리 표시를 위한 초록색 */
}

.lose-color {
  color: red; /* 패배 표시를 위한 빨간색 */
}
@media (max-width: 600px) {
  .ranking-table-container {
    width: 90%;
  }

}
</style>