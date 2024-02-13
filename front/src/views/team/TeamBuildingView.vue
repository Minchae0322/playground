<template>
  <div class="team-building-form-container">
    <h2>创建队伍</h2>
    <div class="form-info">组队</div>

    <form @submit.prevent="submitForm">
    <div class="image-preview">
      <img :src="team.imagePreview || defaultImage" @click="triggerFileInput" class="profile-image" />
      <input type="file" ref="fileInput" @change="handleFileChange" style="display:none" />
      <div class="chang-profile-info">点击换头像</div>
    </div>

      <div class="form-group">
        <label for="teamName">队伍名</label>
        <input type="text" id="teamName" v-model="team.name" placeholder="Enter team name">
      </div>
      <div class="sports-category-container">
        <h4 class="">项目</h4>
        <img src="../../assets/icon-soccerball.png" alt="" :class="{ 'selected': sportsEvent === 'Soccer' }"
             @click="selectCategory('Soccer')">
        <img src="../../assets/icon-basketballBall.png" alt="" :class="{ 'selected': sportsEvent === 'Basketball' }"
             @click="selectCategory('Basketball')">
        <img src="../../assets/icon-badmintonBall.png" alt="" :class="{ 'selected': sportsEvent === 'Badminton' }"
             @click="selectCategory('Badminton')">
        <img src="../../assets/icon-tennisBall.png" alt="" :class="{ 'selected': sportsEvent === 'Tennis' }"
             @click="selectCategory('Tennis')">
        <img src="../../assets/icon-tableTennisBall.png" alt="" :class="{ 'selected': sportsEvent === 'Table_tennis' }"
             @click="selectCategory('Table_tennis')">
      </div>


      <div class="form-group">
        <label for="teamLeader">队长</label>
        <div class="leader-container">
        <img class="leaderImg" :src=leader.userProfileImg >
        <div class="leader-name" >{{leader.userNickname}}</div>
        </div>
      </div>
      <div class="form-group">
        <label for="teamGoals">介绍</label>
        <textarea id="teamGoals" v-model="team.description" placeholder="Enter team goals"></textarea>
      </div>
      <button type="submit" @click="write">确定</button>
    </form>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref} from 'vue';
import defaultImage from '../../assets/img.png';
import axios from "axios";
import router from "@/router";

const leader = ref({
  userNickname: '',
  userProfileImg: '',
})
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const sportsEvent = ref("Soccer")
const fileInput = ref(null);
onMounted(() => {
  // Check if the initial page number is provided in the route query
 getLeaderProfile()
});

const selectCategory = function (category) {
  sportsEvent.value = category; // 'Soccer' 또는 'Baseball'로 설정됩니다.
  console.log(sportsEvent.value)
};

const team = ref({
  name: '',
  description: '',
  profilePicture: 'null',
  imagePreview: defaultImage,
});

const getLeaderProfile = function () {
  validateAccessToken()
  const accessToken = getAccessToken();
  if (accessToken) {
    axios.get(`${apiBaseUrl}/user/info`, {
      headers: {
        'Authorization': accessToken
      }
    }).then(response => {
      leader.value.userNickname = response.data.userNickname
      leader.value.userProfileImg =  `data:image/jpeg;base64,${response.data.userProfileImg}`;
    });
  }
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const handleFileChange = (e) => {
  const file = e.target.files[0];
  team.value.profilePicture = file;

  const reader = new FileReader();
  reader.onload = (e) => {
    team.value.imagePreview = e.target.result;
  };
  reader.readAsDataURL(file);
};

const write = async function () {
  // FormData 인스턴스 생성
  const formData = new FormData();

  // 팀 정보 추가
  formData.append("team", new Blob([JSON.stringify({
    teamName: team.value.name,
    teamDescription: team.value.description,
    sportsEvent: sportsEvent.value
  })], {
    type: "application/json"
  }));

  // 파일이 존재하는 경우에만 파일을 FormData에 추가
  if (team.value.profilePicture) {
    formData.append("imageFile", team.value.profilePicture);
  }

  // Axios로 POST 요청 보내기
  try {
    const response = await axios.post(`${apiBaseUrl}/team/build`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem("accessToken")
      },
    })
    const teamId = response.data.teamId;
    alert("成功")
    router.replace({name: 'teamInfo', params: {teamId: teamId}})
  } catch (e) {
    const errorMessage = extractErrorMessage(error);
    alert(errorMessage);
  }
};

// 에러 메시지 추출 함수
function extractErrorMessage(error) {
  if (error.response && error.response.data) {
    if (error.response.data.errors) {
      // 여러 에러 메시지가 있는 경우 (예: 유효성 검사 실패)
      return error.response.data.errors.map(e => e.message).join('\n');
    } else if (error.response.data.message) {
      // 단일 에러 메시지가 있는 경우
      return error.response.data.message;
    }
  }
  return '글 작성 중 문제가 발생했습니다.'; // 기본 에러 메시지
}

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
.team-building-form-container {
  min-width: 400px;
  width: 35%;
  height: 90%;
  margin: 0 auto 100px auto;
  padding: 20px 30px 0 30px;
  border: 1px solid #ddd;
  background-color: var(--white);
  border-radius: 8px;
  color: var(--text-black);
}



.form-info {
  color: var(--text-hint);
  font-size: 12px;
  font-family: MiSans-Normal,sans-serif;
}

.chang-profile-info {
  font-size: 8px;
  color: var(--text-hint);

}

.leaderImg {
  width: 40px;
  height: 40px;
  margin: 5px 10px;
  border-radius: 50%;
  border: 1px solid var(--text-hint-light);
  cursor: pointer;
}

.team-name {
  text-align: center;
  margin-bottom: 20px;
  font-weight: bold;
  letter-spacing: 5px;
  font-family: primary-font,sans-serif;
}

.team-building-form-container h1 {
  text-align: start;
  font-weight: bold;
}

.team-building-form-container h4 {
  font-weight: bold;
  font-family: primary-font,sans-serif;
}

.leader-container {
  display: flex;
  margin-bottom: 10px;
}

.leader-name {
  margin: auto 5px;
  font-family: MiSans-Medium,sans-serif;
  font-size: 14px;
}

.file-upload-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.file-input {
  width: 1px;
  height: 1px;
  opacity: 0;
  overflow: hidden;
  position: absolute;
  z-index: -1;
}

.file-upload-button {
  background-color: #000000; /* Green */
  color: white;

  border: none;
  width: 25%;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  margin-bottom: 10px;
}



.sports-category-container {
  display: flow;

}
.selected {
  border: 3px solid #e0c200; /* 또는 원하는 색상 코드 */
}

/* 이미지 기본 스타일 */

.sports-category-container img {
  width: 40px;
  height: 40px;
  margin: 10px;
  border-radius: 50%;
  cursor: pointer;
  transition: border-color 0.3s; /* 부드러운 테두리 색상 전환 효과 */
}
.team-building-form-container p {
  text-align: start;
  color: #838383;
  font-size: 12px;
  margin-bottom: 10px;
}

.form-group {
  margin-bottom: 5px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 10px;
}

.form-group input[type="text"],
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #e3e3e3;
  border-radius: 4px;
  margin-bottom: 10px;
}

.image-preview {
  display: flow;
  text-align: center;
  justify-content: center; /* Centers the child horizontally */
  align-items: center; /* Centers the child vertically */
  margin-bottom: 15px; /* Optional: adds some space below the image preview */
}


.form-group input[type="text"] {
  height: 35px;
}

.profile-image {
  width: 100px;
  height: 100px;
  background-color: #eee;
  border-radius: 25%;
  border: 2px solid var(--text-hint-light);
  display: inline-block;
}

.image-preview h4 {
  font-size: 10px;
  color: #838383;
  margin-bottom: 10px;
}


.form-group textarea {
  height: 80px;
  resize: vertical;
}

button {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  background-color: #064183;
  color: white;
  border: none;
  font-family: gothic-bold,sans-serif;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.leader-container h3 {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>