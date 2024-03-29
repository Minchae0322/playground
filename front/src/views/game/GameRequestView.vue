<template>

  <div class="page-title-container">
    <h2>比赛请求</h2>
    <p>Check out our games request</p>
    <div class="page-title-container-border"></div>
  </div>
  <div v-if="pendingRequests && pendingRequests.length >= 1" class="team-requests-container">
    <div v-for="request in pendingRequests" :key="request.id" class="team-request-group">
      <h3>{{ request.gameName }}</h3>
      <div class="request-info-container">
        <div class="user-avatar">
          <img class="user-profile-img-request" :src="getImageUrl(request.userProfileImg)">
        </div>
        <div class="user-info-request">
          <div class="user-name-request">{{ request.username }}</div>
          <div class="user-requestTime-request">{{ request.requestTime }}</div>
          <div class="user-requestType-request">{{ request.requestType_cn }}</div>
        </div>
        <div class="actions-request">
          <button @click="rejectRequest(request.requestId, request.requestType)" class="reject">拒绝</button>
          <button @click="acceptRequest(request.requestId, request.requestType)" class="accept">接受</button>
        </div>
      </div>
      <div class="request-border">
        <hr>
      </div>
    </div>

  </div>
  <div v-else>
    <div class="teamRequest-notExist">没有请求</div>
  </div>
</template>

<script setup>
import {getCurrentInstance, inject, onMounted, ref} from 'vue';
import axios from 'axios';
import {useRouter} from "vue-router";
import router from "@/router";
import UserInfoView from '../UserInfoView.vue'
import defaultImage from "@/assets/img.png";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

// Ref for reactive data
const pendingRequests = ref([]);
const frontBaseUrl = inject('frontBaseUrl');
const getImageUrl = (file) => {
  return frontBaseUrl + file;
};

onMounted(() => {
  fetchPendingRequests()
})


// Fetch data from server
const fetchPendingRequests = async () => {
  await validateAccessToken()
  try {
    const response = await axios.get(`${apiBaseUrl}/user/pending/request/game`,  {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    pendingRequests.value = response.data; // Assuming the data is directly the list of requests
    pendingRequests.value = response.data.map(request => {
      updateRequestType(request, request.requestType);
      return {
        ...request,
        userProfileImg: request.userProfileImg ? request.userProfileImg : defaultImage,
      };
    });
  } catch (error) {
    console.error("There was an error fetching the pending requests: ", error);
    // Handle error appropriately
  }
};

const updateRequestType = (request, type) => {
  if (type === 'friendlyGameJoin') {
    request.requestType_cn = '友谊赛';
  } else if (type === 'soloGameJoin') {
    request.requestType_cn = '竞争赛个人';
  } else if(type === 'teamGameRegistration') {
    request.requestType_cn = '竞争赛组队';
  } else if(type === 'teamGameJoin') {
    request.requestType_cn = '竞争赛加入团队';
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
      alert('接受请求成功');
      router.go(0)
    }
  } catch (error) {
    console.error("请求错误 ", error);
    // 사용자에게 실패를 알리는 메시지를 표시합니다.
    alert('失败接受请求，问题 : jmcabc1216@gmail.com');
  }
};

const rejectRequest = async (requestId, requestType) => {
  await validateAccessToken();
  try {
    const response = await axios.delete(`${apiBaseUrl}/game/reject/${requestId}/${requestType}`,  {
      headers: {
        'Authorization': getAccessToken()
      }
    });
    if (response.status === 200) {
      // 성공적인 요청 후 처리. 예를 들어, 요청 목록을 갱신하거나 사용자에게 알림을 표시할 수 있습니다.
      // 예: pendingRequests.value = pendingRequests.value.filter(request => request.requestId !== requestId);
      alert('拒绝请求成功');
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
.request-border {
  margin: 10px 0;

}

.user-requestType-request {
  color: #00b7ff;
}

</style>