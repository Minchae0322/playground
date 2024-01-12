
<template>
  <div class="team-requests-container">
    <h2>Team Join Requests</h2>
    <!-- teamName 별로 반복 -->
    <div v-for="(requests, teamName) in joinRequests" :key="teamName" class="team-request-group">
      <h3>{{ teamName }}</h3>
      <!-- 해당 teamName의 요청들을 반복 -->
      <div v-for="request in requests" :key="request.requestId" class="request">
        <div class="user-avatar">
          <span class="avatar-label">{{ request.userId }}</span>
        </div>
        <div class="user-info">
          <div class="user-name">{{ request.userName }}</div>
          <div class="user-role">{{ request.role }}</div>
        </div>
        <div class="actions">
          <button class="accept">Accept</button>
          <button class="reject">Reject</button>
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
import UserInfoView from '../views/UserInfoView.vue'


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
      acc[request.teamName].push(request);
      return acc;
    }, {});
    joinRequests.value = groupedRequests;
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
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
.team-requests-container {
  max-width: 800px;
  margin: auto;
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

h3 {
  color: #333;
  margin-bottom: 10px;
}

.request {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ececec;
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
  font-weight: bold;
}

.user-role {
  color: #666;
}

.actions button {
  padding: 10px 20px;
  margin-right: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.accept {
  background-color: #5cb85c;
  color: white;
}

.reject {
  background-color: #d9534f;
  color: white;
}

.actions button:hover {
  opacity: 0.8;
}
</style>