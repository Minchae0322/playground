<template>
  <div class="card">
    <div class="header">
      <img :src="user.userProfileImg || defaultImage" @click="triggerFileInput" class="profile-image" />
      <input type="file" ref="fileInput" @change="handleFileChange" style="display:none" />
      <p>사진을 클릭하여 프로필을 변경하세요.</p>
      <h2>{{ user.userNickname }}</h2>

      <span class="verified">Verified</span>
    </div>
    <p>This is a short bio about the user. It provides some general information about the user's interests, hobbies, or profession.</p>
    <button>Edit Profile</button>
    <button>View Public Profile</button>
    <div class="teams">
      <h3>Teams</h3>
      <ul>
        <li>Team 1</li>
        <li>Team 2</li>
        <li>Team 3</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import defaultImage from '../assets/img.png';
import {useRouter} from "vue-router";

const apiBaseUrl = "http://localhost:8080";
const router = useRouter();
const userProfileImg = ref('');
const user = ref({
  userNickname: '',
  userProfileImg: ref('')
})

onMounted(() => {
  // Check if the initial page number is provided in the route query
  getUserInfo()
});
const getUserInfo = function () {
  validateAccessToken()
  axios.get(`${apiBaseUrl}/user/info`,
      {
        headers: {
          'Authorization': localStorage.getItem("accessToken")
        }
      }
  ).then(response => {
    user.value.userNickname = response.data.userNickname
    user.value.userProfileImg = `data:image/jpeg;base64,${response.data.userProfileImg}`;
  });

};


const fileInput = ref(null);

const triggerFileInput = () => {
  fileInput.value.click();
};

const handleFileChange = async (event) => {
  await validateAccessToken()
  const file = event.target.files[0];
  if (!file) {
    console.log('파일을 선택해주세요.');
    return;
  }

  const formData = new FormData();
  formData.append('image', file); // 'image'는 백엔드에서 기대하는 필드명입니다.

  try {
    const response = await axios.post(`${apiBaseUrl}/user/profile-img/update`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem("accessToken")
      },
    });
    router.go(0)
    console.log('업로드 성공', response.data);
  } catch (error) {
    console.error('업로드 실패', error);
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

.card {
  width: 300px;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  font-family: 'Arial', sans-serif;
}

.header {
  text-align: center;
  margin-bottom: 20px;

}

.header p {
  font-size: 10px;
  color: #838383;
}

.profile-image {
  width: 80px;
  height: 80px;
  background-color: #eee;
  border-radius: 50%;
  display: inline-block;
  margin-bottom: 10px;
}

h2 {
  margin: 0;
}

.verified {
  background: green;
  color: white;
  padding: 2px 6px;
  border-radius: 5px;
  display: inline-block;
  margin-top: 10px;
}

button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  background-color: #f5f5f5;
  cursor: pointer;
}

button:hover {
  background-color: #e1e1e1;
}

.teams h3 {
  margin-top: 20px;
}

.teams ul {
  list-style: none;
  padding: 0;
}

.teams li {
  background-color: #f5f5f5;
  padding: 5px;
  margin-top: 5px;
  border-radius: 5px;
}
</style>