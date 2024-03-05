<template>
  <div class="team-container-teamInfo">
    <div class="team-header-teamInfo">
      <img :src=team.teamProfileImg class="team-header-img-teamInfo">
      <div class="team-header-info-teamInfo">
        <h1>{{ team.teamName }}</h1>
        <p class="team-header-info-type-teamInfo">{{ team.teamSportsEvent }}</p>
      </div>
      <button v-if="isTeamLeader" class="button-teamJoin-teamInfo" :class="{'button-teamLeader': isTeamLeader}"
              @click="deleteTeam">删除
      </button>
      <button v-else-if="isTeamMember" class="button-teamJoin-teamInfo">已加入</button>
      <button v-else class="button-teamJoin-teamInfo" @click="openModal">加入</button>
    </div>


    <div class="team-history-teamInfo">
      <h2>队伍介绍</h2>
      <p>{{ team.description === null ? '没有介绍' : team.description }}</p>
    </div>
    <div class="team-players-teamInfo">
      <h2>成员</h2>
      <div v-for="user in teamMembers" :key="user.userId">
        <table>
          <thead>
          <tr>
            <th>头像</th>
            <th>队员名</th>
            <th>角色</th>
          </tr>
          </thead>
          <tbody>
          <tr>
              <img :src="user.userProfileImg || defaultImage" class="team-players-img-teamInfo"/>
              <td>{{ user.userNickname }}</td>
              <td>{{ user.userRole }}</td>
          </tr>
          <!-- More players as needed -->
          </tbody>
        </table>
      </div>

    </div>
    <div class="team-achievements">
      <h2>Achievements</h2>
      <ul>
        <li>准备中</li>
      </ul>
    </div>

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <label for="introduction">介绍我（30个字内）</label>
        <textarea rows="4" placeholder="介绍我" :maxlength="30" v-model="introduction"></textarea>
        <button class="button-teamJoin-teamInfo" @click="submitIntroduction">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import defaultImage from "@/assets/img.png";


const router = useRouter();
const isTeamMember = ref(false);
const team = ref({
  teamProfileImg: ref(''),
  teamName: '',
  teamSportsEvent: ref(''),
  leaderName: '',
  leaderId: '',
});

const isTeamLeader = ref(false);

const teamMembers = ref([{
  userProfileImg: ref(''),
  userNickname: '',
  userRole: '',
  userId: '',

}])
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

const props = defineProps({
  teamId: {
    type: Number,
    required: true,
  }
})
onMounted(async () => {
  await checkTeamMember();
  await checkTeamLeader();
  getTeamInfo()
  getTeamMembers()
});

const showModal = ref(false);
const introduction = ref('');

const closeModal = () => {
  showModal.value = false;
  introduction.value = '';
};

const openModal = () => {
  showModal.value = true;
};

const submitIntroduction = async () => {
  await validateAccessToken()
  try {
    await axios.post(`${apiBaseUrl}/team/join/teamJoin`, {
          teamId: props.teamId,
          introduction: introduction.value,
        },
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    alert("请求成功");
    closeModal();
  } catch (error) {
    alert(error.response.data.message);
  }
};

const checkTeamLeader = async () => {
  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/team/${props.teamId}/check/leader`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    isTeamLeader.value = response.data;
  } catch (error) {
  }
};

const deleteTeam = async () => {
  const isConfirm = confirm("团队所有的信息都会消失。\n" +
      "确定要删除吗？")
  if (!isConfirm) {
    return
  }
  await validateAccessToken()

  try {
    const response = await axios.delete(`${apiBaseUrl}/team/${props.teamId}/delete`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    await router.replace({name: 'myTeam'})
  } catch (error) {
    alert(error.response.data.message)
  }
};

const checkTeamMember = async () => {
  await validateAccessToken()

  try {
    const response = await axios.get(`${apiBaseUrl}/team/${props.teamId}/check/member`,
        {
          headers: {
            'Authorization': getAccessToken()
          }
        }
    );
    isTeamMember.value = response.data;
  } catch (error) {
  }
};

const getTeamMembers = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/team/${props.teamId}/members`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    teamMembers.value = response.data.map(user => ({
      userId: user.userId,
      userNickname: user.userNickname,
      userRole: user.userRole,
      userProfileImg: user.userProfileImg ? `data:image/jpeg;base64,${user.userProfileImg}` : defaultImage
    }));
  });
};
const getTeamInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/team/${props.teamId}/info`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    team.value = response.data
    team.value.teamProfileImg = response.data.teamProfileImg ? `data:image/jpeg;base64,${response.data.teamProfileImg}` : defaultImage
  });

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
td {
  font-family: MiSans-Normal,sans-serif;
}
.team-container-teamInfo {
  min-width: 1000px;
  width: 90%;
  margin: 20px auto;

}

.team-header-teamInfo {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}


.team-header-info-teamInfo h1 {
  margin: 0;
  font-size: 2em;
}

.team-header-info-type-teamInfo {
  color: #666;
  font-size: 1em;
}


.team-header-img-teamInfo {
  border-radius: 25%;
  width: 100px;
  height: 100px;
  margin-right: 20px;
  background-color: #ddd;
}

.button-teamJoin-teamInfo {
  background: var(--accent-color);
  border: none;
  padding: 10px 20px; /* 버튼 패딩 조정 */
  color: white; /* 텍스트 색상 */
  border-radius: 4px;
  width: 200px;
  margin: 10px 18px 10px auto;
  text-align: center; /* 왼쪽 정렬 */
}

.button-teamJoin-teamInfo:hover {
  border: none;
  background: var(--secondary-color);
}

.button-teamLeader {
  border: none;
  background-color: #f44336; /* 빨간색 배경 */
  color: white; /* 텍스트 색상 */
}

.button-teamLeader:hover {
  background-color: #d32f2f; /* 마우스 오버시 어두운 빨간색 */
}

.team-history-teamInfo {
  background: var(--white); /* Light grey background */
  border: 1px solid #ddd; /* Light grey border */
  border-radius: 8px; /* Rounded corners */
  padding: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
  font-family: MiSans-Medium, sans-serif;
}

.team-history-teamInfo h2 {
  font-size: 1.5em;
  color: #333; /* Darker text color for the title */
  margin-bottom: 10px;

}

.team-history-teamInfo p {
  font-size: 1em;
  font-family: MiSans-Light, sans-serif;
  color: #555; /* Slightly lighter text color for the paragraph */
  line-height: 1.6; /* More readable line height */
}


.team-achievements {
  font-family: MiSans-Medium, sans-serif;
  border-radius: 8px;
  margin-top: 20px;
  background: var(--white);
  padding: 20px;
}

.team-players-teamInfo {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 20px;
  border-radius: 8px; /* Rounded corners */
  background: var(--white);
}

.team-players-teamInfo h2 {
  font-size: 1.5em;
  color: #333;
  font-family: MiSans-Medium, sans-serif;
  margin-bottom: 10px;
}

.team-players-teamInfo table {
  width: 100%;
  border-collapse: collapse;

  margin-top: 20px; /* Space between title and table */
}


.team-players-teamInfo th {
  font-family: MiSans-Light, sans-serif;
}

.team-players-teamInfo th,
.team-players-teamInfo td {
  text-align: left;
  padding: 12px 8px; /* Adjust the padding to match the design */
  border-bottom: 1px solid #ddd; /* Light gray border for separation */
}

.team-players-teamInfo img {
  margin: 10px 0 10px 20px;
  width: 50px;
  height: 50px;
  background-color: #eee;
  border-radius: 50%;
  display: flex;
  align-items: end;
  justify-content: end;
  box-shadow: 0 3px 6px 0 rgba(29, 34, 53, .08);
}

.team-players-teamInfo th {
  background: var(--white); /* Light gray background for headers */
  border-bottom: 2px solid #eaeaea; /* Slightly darker border for headers */
}

.team-players-teamInfo tbody tr:hover {
  background-color: #f2f2f2; /* Slightly darker background on hover */
}

.team-players-teamInfo tbody td {

  color: #555; /* Slightly darker text color for content */
}

/* Remove the border from the last row to match the design */
.team-players-teamInfo tr:last-child td {
  border-bottom: none;
}


.team-achievements ul {
  list-style: none;
  padding: 0;
  font-family: MiSans-Light, sans-serif;
}

.team-achievements li {

  background: #f2f2f2;
  margin-bottom: 5px;
  padding: 10px;
  border-radius: 5px;
}
.modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  cursor: pointer;
}

/* 텍스트 입력 스타일 */
textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}


@media (max-width: 600px) {
  .team-container-teamInfo {
    min-width: 400px;
    margin: 20px auto 100px auto;
    width: 90%;
  }

  .button-teamJoin-teamInfo {
    padding: 10px 20px;
    margin: 0 0 0 auto;
  }

  .button-teamJoin-teamInfo {
    width: 120px;
  }

}
</style>