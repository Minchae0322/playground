
<template>

  <div class="info-container">
    <h2>团队请求</h2>
    <p>Check out our teams request</p>
    <div class="info-container-border"></div>
  </div>
  <div class="team-requests-container">

    <div v-for="(requests, teamName) in joinRequests" :key="teamName" class="team-request-group">
      <h3>{{ teamName }}</h3>
      <div v-for="request in requests" :key="request.requestId" class="request" @click="toggleIntroduction(request)">
        <div class="request-info-container">
          <div class="user-avatar">
          </div>
          <div class="user-info">
            <div class="user-name">{{ request.userName }}</div>
            <div class="user-requestTime">{{ request.requestTime }}</div>
          </div>
          <div class="toggle-info">눌러서 설명보기</div>
          <div class="actions">
            <button class="reject">Reject</button>
            <button class="accept">Accept</button>
          </div>
        </div>
        <div v-if="request.isExpanded" class="introduction">
          <p>소개 : {{ request.introduction }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router";
import UserInfoView from '../UserInfoView.vue'


const apiBaseUrl = "http://localhost:8080";



const joinRequests = ref([{
  requestId: '',
  introduction: '',
  teamName: '',
  userName: '',
  userId: '',
  requestTime: '',
}]);

onMounted(() => {
  fetchPendingRequests('teamJoin')
})
//todo chatGpt 에 있는 dto 한 번 읽어보기.
const fetchPendingRequests = async (requestType) => {
  await validateAccessToken()
  try {
    const response = await axios.post(`${apiBaseUrl}/user/pending/request/team/${requestType}`, {
    }, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    const groupedRequests = response.data.reduce((acc, request) => {
      // 'teamName'을 키로 사용하여 그룹화
      if (!acc[request.teamName]) {
        acc[request.teamName] = [];
      }
      acc[request.teamName].push({...request,  isExpanded: false});
      return acc;
    }, {});
    joinRequests.value = groupedRequests;
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
  }
};

const toggleIntroduction = (request) => {
  request.isExpanded = !request.isExpanded;
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
  margin-left: auto;
  margin-right: 100px;
  width: 70%;
  border-bottom: 1px solid var(--text-hint);
}

.info-container p {
  font-size: 0.8rem;
  color: #666;
  margin-right: auto;
  margin-left: 100px;
  margin-bottom: 20px;
  font-family: MiSans-Normal,sans-serif;
}

.team-requests-container {
  min-width: 1100px;
  width: 90%;
  margin: 0 0 40px 40px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

h2 {
  text-align: center;
  color: #333;
}

.team-request-group {
  border-top: 1px solid #ccc;
  padding-top: 20px;
}

.team-request-group h3 {
  color: var(--text-black);
  margin-bottom: 20px;
  font-size: 21px;
  font-family: MiSans-Heavy,sans-serif;
}

.request {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ececec;
}

.request-info-container {
  width: 100%;
  display: flex;
}

.user-avatar {
  width: 50px;
  height: 50px;
  background-color: #ddd;
  border-radius: 50%;
  text-align: center;
  line-height: 50px;
  margin-right: 10px;
  font-weight: bold;
  color: #333;
}

.avatar-label {
  display: inline-block;
  vertical-align: middle;
  line-height: normal;
}

.user-info {
  flex-grow: 1;
}

.user-name {
  font-family: MiSans-Semibold,sans-serif;
  color: var(--text-black);
}

.user-requestTime {
  color: var(--text-hint);
  font-size: 12px;
  font-family: MiSans-Normal,sans-serif;
}

.toggle-info {
  font-size: 9px;
  color: var(--text-hint);
  display: flex;
  margin-top: auto; /* 상단 여백을 자동으로 설정하여 하단에 바짝 붙게 함 */
  margin-right: 10px; /* 우측 여백 추가 */
  text-align: right; /* 텍스트 우측 정렬 */
  flex-grow: 1.2;
  justify-content: left;
}

.introduction {
  padding: 10px 10px 10px 20px;
  width: 100%;
  margin-top: 10px;
  background-color: var(--background-color-gray);

  border-radius: 5px;
  color: var(--text-hint-dark);
  font-size: 12px;
  font-family: MiSans-Medium,sans-serif;
}


.actions button {
  width: 70px;
  height: 35px;
  text-align: center;
  justify-content: center;
  margin-right: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.accept {
  background-color: var(--primary-color);
  color: white;
  border: none;
}

.reject {
  background-color: white;
  color: black;
  border: 1px solid var(--text-hint-light);
}

.actions button:hover {
  opacity: 0.6;
}
</style>