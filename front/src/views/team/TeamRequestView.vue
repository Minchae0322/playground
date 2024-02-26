
<template>
  <div class="page-title-container">
    <h2>团队请求</h2>
    <p>Check out our teams request</p>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="joinRequests && joinRequests.length >= 1" class="team-requests-container">
    <div v-for="(requests, teamName) in joinRequests" :key="teamName" class="team-request-group">
      <h3>{{ teamName }}</h3>
      <div v-for="request in requests" :key="request.requestId" class="request">
        <div class="request-info-container">
          <div class="user-avatar">
            <img class="user-profile-img-request" :src="request.userProfileImg">
          </div>
          <div class="user-info-request" @click="toggleIntroduction(request)">
            <div class="user-name-request">{{ request.userName }}</div>
            <div class="user-requestTime-request">{{ request.requestTime }}</div>
          </div>
          <div class="toggle-info-request" @click="toggleIntroduction(request)">点击看介绍 ></div>
          <div class="actions-request">
            <button @click="rejectRequest(request.requestId)" class="reject">Reject</button>
            <button @click="acceptRequest(request.requestId)" class="accept">Accept</button>
          </div>
        </div>
        <div v-if="request.isExpanded" class="introduction-request">
          <p>介绍 : {{ request.introduction }}</p>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="teamRequest-notExist">没有请求</div>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref} from 'vue';
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router";
import UserInfoView from '../UserInfoView.vue'
import defaultImage from "@/assets/img.png";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;



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
      acc[request.teamName].push({
        ...request,
        userProfileImg: request.userProfileImg ? `data:image/jpeg;base64, ${request.userProfileImg}` : defaultImage,
        isExpanded: false
      });
      return acc;
    }, {});
    joinRequests.value = groupedRequests;
    console.log(joinRequests.value)
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
  }
};

const toggleIntroduction = (request) => {
  request.isExpanded = !request.isExpanded;
};

const acceptRequest = async (requestId) => {
  await validateAccessToken()

  try {
    const response = await axios.patch(`${apiBaseUrl}/team/accept/${requestId}/teamJoin`, {
    }, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    alert("success")
    await fetchPendingRequests("teamJoin");
  } catch (error) {
    alert(error.response.data.message)
    // Handle error appropriately
  }
};

const rejectRequest = async (requestId) => {
  await validateAccessToken()

  try {
    const response = await axios.patch(`${apiBaseUrl}/team/reject/${requestId}/teamJoin`, {
    }, {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    alert("success")
    await fetchPendingRequests("teamJoin");
  } catch (error) {
    alert(error.response.data.message)
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


<style>
.team-requests-container {
  min-width: 1100px;
  width: 90%;
  margin: 0 0 40px 40px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.team-request-group {
  padding-top: 20px;
}

.team-request-group h3 {
  color: var(--text-black);
  margin-bottom: 10px;
  font-size: 21px;
  font-family: MiSans-Heavy,sans-serif;
}

.request {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
  padding: 0 20px 20px 20px;
  border-bottom: 1px solid #ececec;
}

.request-info-container {
  width: 100%;
  display: flex;
}


.user-profile-img-request {
  width: 50px;
  height: 50px;
  background-color: #ddd;
  border-radius: 50%;
  line-height: 50px;
  margin-right: 15px;
}

.user-info-request {
  flex-grow: 1;
}

.user-name-request {
  font-family: MiSans-Semibold,sans-serif;
  color: var(--text-black);
}

.user-role-request {
  color: var(--text-hint);
  font-size: 12px;
  font-family: MiSans-Normal,sans-serif;
}

.toggle-info-request {
  font-size: 9px;
  color: var(--text-hint);
  display: flex;
  font-family: MiSans-Medium,sans-serif;
  margin-top: auto; /* 상단 여백을 자동으로 설정하여 하단에 바짝 붙게 함 */
  margin-right: 10px; /* 우측 여백 추가 */
  text-align: right; /* 텍스트 우측 정렬 */
  flex-grow: 1.2;
  justify-content: left;
  cursor: pointer;
}

.introduction-request {
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
  width: 100px;
  background-color: var(--primary-color);
  color: white;
  border: none;
}

.reject {
  width: 100px;
  background-color: white;
  color: black;
  border: 1px solid var(--text-hint-light);
}

.actions button:hover {
  opacity: 0.6;
}


.teamRequest-notExist {
  text-align: center;
  width: 100%;
  font-family: MiSans-Normal,sans-serif;
  color: var(--text-black);
  margin-top: 250px;
  align-content: center;
  justify-content: center;
}

@media (max-width: 600px) {
  .team-requests-container {
    min-width: 400px;
    width: 95%;
    margin: 0 auto;
  }

  .toggle-info-request {
    text-align: start;
  }


}
</style>