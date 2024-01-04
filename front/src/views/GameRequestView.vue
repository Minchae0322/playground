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
          <td>{{ request.gameName }}</td>
          <td>{{ request.username }}</td>
          <td>{{ request.requestType }}</td>
          <td>{{ request.requestTime }}</td>
          <td class="action-buttons">
            <button class="action-button action-button-reject">Reject</button>
            <button class="action-button action-button-accept">Accept</button>
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


const apiBaseUrl = "http://localhost:8080";

// Ref for reactive data
const pendingRequests = ref([]);

onMounted(() => {
  fetchPendingRequests()
})


// Fetch data from server
const fetchPendingRequests = async () => {
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
.main-container {
  width: 80%;
  padding: 16px;
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
  color: #838383;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  border-bottom: 1px solid #ccc;
  text-align: left;
}

/* 테이블 바디 스타일 */
.table tbody td {
  padding: 12px;
  border-bottom: 1px solid #eee;
  text-overflow: ellipsis; /* 넘치는 텍스트를 말줄임표로 표시 */
  overflow: hidden;
  white-space: nowrap;
}

/* 테이블 행 호버 스타일 */
.table tbody tr:hover {
  background-color: #f1f1f1;
}

/* 상태 라벨 스타일 */
.status-pending,
.status-approved,
.status-declined {
  text-align: center;
  padding: 4px 8px;
  border-radius: 4px;
  color: white;
  font-weight: bold;
}



.status-approved {
  background-color: #28a745; /* 승인 상태의 배경색 */
}

.status-declined {
  background-color: #dc3545; /* 거절 상태의 배경색 */
}

/* 액션 버튼 스타일 */
.action-buttons {
  display: flex;
  justify-content: start;
  gap: 8px;
}

.action-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.action-button-accept {
  background-color: #28a745; /* 승인 버튼의 배경색 */
}

.action-button-reject {
  background-color: #dc3545; /* 거절 버튼의 배경색 */
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