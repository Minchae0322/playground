
<template>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>팀 신청 관리</title>
  <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="container">
  <header>
    <h1>팀 신청 관리</h1>
  </header>
  <div class="search-box">
    <input type="text" placeholder="신청 검색...">
  </div>
  <div class="applications">
    <div class="application-header">
      <span>사용자 이름</span>
      <span>사용자 소개</span>
      <span>팀 이름</span>
      <span>신청 상태</span>
      <span>작업</span>
    </div>
    <!-- 신청 내역 반복 -->
    <div class="application">
      <div class="user-info">
        <span>John Doe</span>
        <span>경험 많은 소프트웨어 엔지니어, 팀 협업을 중시함.</span>
      </div>
      <div class="team-info">
        <span>Team Alpha</span>
        <span>대기 중</span>
      </div>
      <div class="actions">
        <button>보기</button>
        <button>승인</button>
        <button>거절</button>
      </div>
    </div>
    <!-- 다른 신청 내역도 같은 방식으로 추가 -->
  </div>
</div>
</body>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router";
import UserInfoView from '../views/UserInfoView.vue'


const apiBaseUrl = "http://localhost:8080";
const groupedRequests = ref({});


onMounted(() => {
  fetchPendingRequests('teamGameJoin')
})

const fetchPendingRequests = async (requestType) => {
  await validateAccessToken()
  try {
    const response = await axios.post(`${apiBaseUrl}/user/pending/request/${requestType}`, {
      teamId: 1
    }, {
      headers: {
        'Authorization': getAccessToken()
      }
    });

    const data = response.data;
    groupRequestsByTeam(data);
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
  }
};

const groupRequestsByTeam = (requests) => {
  const grouped = requests.reduce((acc, request) => {
    const teamName = request.teamName;
    if (!acc[teamName]) {
      acc[teamName] = [];
    }
    acc[teamName].push(request);
    return acc;
  }, {});

  groupedRequests.value = grouped;
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
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: #f9f9f9;
  margin: 0;
  padding: 20px;
}

.container {
  max-width: 1000px;
  margin: auto;
  background: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

header h1 {
  margin: 0;
  padding-bottom: 20px;
  color: #333;
  text-align: center;
}

.search-box input[type="text"] {
  width: calc(100% - 40px);
  padding: 10px;
  margin: 0 20px 20px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.application-header {
  display: grid;
  grid-template-columns: 2fr 3fr 1fr 1fr 1fr;
  background-color: #f7f7f7;
  padding: 10px 0;
  border-bottom: 2px solid #e7e7e7;
  font-weight: 600;
}

.application {
  display: grid;
  grid-template-columns: 2fr 3fr 1fr 1fr 1fr;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e7e7e7;
}

.user-info span,
.team-info span {
  padding: 0 20px;
}

.actions button {
  border-radius: 4px;
  border: 1px solid transparent;
  padding: 5px 10px;
  margin: 0 10px;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.actions button.view {
  background-color: #f0f0f0;
  color: #333;
}

.actions button.approve {
  background-color: #5cb85c;
  color: white;
}

.actions button.reject {
  background-color: #d9534f;
  color: white;
}

.actions button:hover {
  opacity: 0.85;
}

@media (max-width: 768px) {
  .application-header,
  .application {
    grid-template-columns: repeat(5, 1fr);
  }

  .actions button {
    padding: 5px;
    margin: 2px;
    font-size: 0.8em;
  }
}

</style>