<template>
  <main class="main-container">

    <!-- ...other components... -->

    <!-- Table container -->
    <div class="table-container">
      <table class="table">
        <!-- Table Head -->
        <thead>
        <tr>
          <th>게임</th>
          <th>참가자</th>
          <th>Request Type</th>
          <th>요청시간</th>
          <th>Actions</th>
        </tr>
        </thead>
        <!-- Table Body -->
        <tbody>
        <tr v-for="request in pendingRequests" :key="request.requestId"
            :class="{'solo-game': request.requestType === 'soloGameJoin',
               'team-game': request.requestType === 'teamGameJoin',
               'registration-game': request.requestType === 'teamGameRegistration'}">
          <router-link :to="{ name:'gameInfo', params: { gameId: request.gameId } }">
            <td>{{ request.gameName }}</td>
          </router-link>
          <td>{{ request.username }}</td>
          <td>{{ request.requestType }}</td>
          <td>{{ request.requestTime }}</td>
          <td class="action-buttons">
<!--            <button class="action-button action-button-reject" @click="rejectRequest(request.requestId, request.requestType)">Reject</button>-->
            <button class="action-button action-button-accept" @click="acceptRequest(request.requestId, request.requestType)">Accept</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router";
import UserInfoView from '../UserInfoView.vue'


const apiBaseUrl = "http://localhost:8080";

// Ref for reactive data
const pendingRequests = ref([]);

onMounted(() => {
  fetchPendingRequests('teamGameJoin')
  fetchPendingRequests('teamGameRegistration')
  fetchPendingRequests('soloGameJoin')
})


// Fetch data from server
const fetchPendingRequests = async (requestType) => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/user/pending/request`, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    pendingRequests.value = response.data; // Assuming the data is directly the list of requests
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
  }
};

const acceptRequest = async (requestId, requestType) => {
  await validateAccessToken();
  try {
    const response = await axios.patch(`${apiBaseUrl}/game/accept/${requestId}/${requestType}`, {}, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    if (response.status === 200) {
      // 성공적인 요청 후 처리. 예를 들어, 요청 목록을 갱신하거나 사용자에게 알림을 표시할 수 있습니다.
      // 예: pendingRequests.value = pendingRequests.value.filter(request => request.requestId !== requestId);
      alert('Request accepted successfully.');
      router.go(0)
    }
  } catch (error) {
    console.error("There was an error accepting the request: ", error);
    // 사용자에게 실패를 알리는 메시지를 표시합니다.
    alert('Failed to accept the request.');
  }
};

const rejectRequest = async (requestId, requestType) => {
  await validateAccessToken();
  try {
    const response = await axios.patch(`${apiBaseUrl}/game/reject/${requestId}/${requestType}`, {}, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    if (response.status === 200) {
      // 성공적인 요청 후 처리. 예를 들어, 요청 목록을 갱신하거나 사용자에게 알림을 표시할 수 있습니다.
      // 예: pendingRequests.value = pendingRequests.value.filter(request => request.requestId !== requestId);
      alert('Request accepted successfully.');
      router.go(0)
    }
  } catch (error) {
    console.error("There was an error accepting the request: ", error);
    // 사용자에게 실패를 알리는 메시지를 표시합니다.
    alert('Failed to accept the request.');
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

a{
  text-decoration: none;
}
.main-container {
  width: 80%;
  padding: 16px;
  height: 100%;
  margin: auto;
  box-sizing: border-box;
}




/* Different background colors for each request type */
.solo-game {
  background-color: #e0f7fa; /* Light blue for solo game join requests */
}

.team-game {
  background-color: #e8f5e9; /* Light green for team game join requests */
}

.registration-game {
  background-color: #fff3e0; /* Light orange for game registration requests */
}

/* 검색 및 정렬 섹션 스타일 */
.search-and-sort {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

/* 검색 입력 필드 스타일 */
.search-input {
  width: calc(100% - 100px); /* 정렬 버튼의 공간을 고려하여 너비를 조정 */
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px; /* 정렬 버튼과의 간격 */
}

/* 정렬 버튼 스타일 */
.sort-button {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 8px 16px; /* 좌우 패딩을 늘려서 버튼을 더 커 보이게 함 */
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

/* 테이블 컨테이너 스타일 */
.table-container {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow-x: auto;
}

/* 테이블 스타일 */
.table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed; /* 컬럼 너비를 고정하기 위한 스타일 */
}

/* 테이블 헤더 스타일 */
.table thead th {
  background-color: #f9f9f9;
  padding: 12px;
  color:black;
  font-weight: bold;
  padding-left: 24px; /* 기존 padding 값에 더하여 내용을 오른쪽으로 이동 */
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  border-bottom: 2px solid #eee; /* 테두리를 좀 더 두껍게 조정 */
  text-align: left;
}

/* 테이블 바디 스타일 */
.table tbody td {
  padding: 16px; /* 패딩을 더 크게 조정 */
  border-bottom: 1px solid #eee; /* 테두리 색상을 조정 */
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  font-size: 14px; /* 글자 크기 조정 */
  color: #333; /* 글자 색상 조정 */
}



/* 액션 버튼 스타일 */
.action-buttons button {
  padding: 8px 16px; /* 버튼 패딩 조정 */
  margin-right: 8px; /* 버튼 사이 간격 조정 */
  border: none; /* 테두리 제거 */
  border-radius: 4px; /* 둥근 모서리 */
  color: white; /* 텍스트 색상 흰색 */
  font-size: 14px; /* 글자 크기 조정 */
  font-weight: 500; /* 글자 무게 조정 */
  cursor: pointer;
  outline: none; /* 외곽선 제거 */
  box-shadow: none; /* 그림자 제거 */
}

.action-button-reject {
  background-color: #F87171; /* 빨간색 배경 */
}

.action-button-accept {
  background-color: #34D399; /* 녹색 배경 */
}

.action-button:hover {
  opacity: 0.8;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .search-and-sort {
    flex-direction: column;
  }

  .search-input,
  .sort-button {
    width: 100%;
    margin-bottom: 8px;
  }
}

</style>