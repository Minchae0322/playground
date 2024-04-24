<template>

  <div class="team-building-form-container">
    <h2>添加运动场</h2>
    <div class="form-info">运动场</div>

    <form @submit.prevent="submitForm">
      <div class="image-preview">
        <img :src="playground.imagePreview || defaultImage" @click="triggerFileInput" class="profile-image-playground" />
        <input type="file" ref="fileInput" @change="handleFileChange" style="display:none" />
        <div class="chang-profile-info">点击换头像</div>
      </div>

      <div class="form-group">
        <label for="teamName">运动场名</label>
        <input type="text" v-model="playground.name" placeholder="Enter team name">
      </div>

      <select  v-model="selectedMenu">
        <option v-for="campus in campusList" :key="campus.campusId" :value="campus.campusId">
          {{ campus.campusName }}
        </option>
      </select>


      <div class="sports-category-container">
        <h4 class="">项目</h4>
        <img src="../assets/soccer-ball.png" alt="" :class="{ 'selected': sportsEvent === 'Soccer' }"
             @click="selectCategory('Soccer')">
        <img src="../assets/icon-basketballBall.png" alt="" :class="{ 'selected': sportsEvent === 'Basketball' }"
             @click="selectCategory('Basketball')">
        <img src="../assets/icon-badmintonBall.png" alt="" :class="{ 'selected': sportsEvent === 'Badminton' }"
             @click="selectCategory('Badminton')">
        <img src="../assets/icon-tennisBall.png" alt="" :class="{ 'selected': sportsEvent === 'Tennis' }"
             @click="selectCategory('Tennis')">
        <img src="../assets/icon-tableTennisBall.png" alt="" :class="{ 'selected': sportsEvent === 'Table_tennis' }"
             @click="selectCategory('Table_tennis')">
      </div>




      <button type="submit" @click="write">确定</button>
    </form>
  </div>
</template>


<script setup>
import {getCurrentInstance, onMounted, ref} from 'vue';
import axios from "axios";
import defaultImage from '../assets/img.png';
import {useRouter} from "vue-router";


const sportsEvent = ref("Soccer")
const fileInput = ref(null);

const playground = ref({
  name: '',
  description: '',
  profilePicture: 'null',
  imagePreview: defaultImage,
});


const campusList = ref([
  {
    campusId: '1',
  }
]);


const selectedMenu = ref(campusList.value[0].campusId);

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
/*
const handleFileChange = async (event) => {
  await validateAccessToken()
  const file = event.target.files[0];
  if (!file) {
    console.log('파일을 선택해주세요.');
    return;
  }

  const options = {
    quality: 0.6,
    maxWidth: 800,
    maxHeight: 600,
    mimeType: 'image/jpeg',
  };

  const compressedImage = await compressImage(file);

  const formData = new FormData();
  formData.append('image', compressedImage); // 'image'는 백엔드에서 기대하는 필드명입니다.

  try {
    const response = await axios.post(`${apiBaseUrl}/img/update`, formData, {
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
*/
onMounted(async () => {
  // Check if the initial page number is provided in the route query
  await getCampusList()
});
const getCampusList = async () => {
  await validateAccessToken();

  try {
    const response = await axios.get(`${apiBaseUrl}/campus/list/1`, {
      headers: {

        'Authorization': getAccessToken()
      },
    });
    campusList.value = response.data;

  } catch (error) {

  }
};


const selectCategory = function (category) {
  sportsEvent.value = category; // 'Soccer' 또는 'Baseball'로 설정됩니다.
  console.log(sportsEvent.value)
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const handleFileChange = (e) => {
  const file = e.target.files[0];
  playground.value.profilePicture = file;

  const reader = new FileReader();
  reader.onload = (e) => {
    playground.value.imagePreview = e.target.result;
  };
  reader.readAsDataURL(file);
};

const write = async function () {
  // FormData 인스턴스 생성
  const formData = new FormData();

  // 팀 정보 추가
  formData.append("playgroundRequest", new Blob([JSON.stringify({
    playgroundName: playground.value.name,
    sportsEvent: sportsEvent.value,
    campusId: selectedMenu.value,

  })], {
    type: "application/json"
  }));

  const compressedImage = await compressImage(playground.value.profilePicture);
  // 파일이 존재하는 경우에만 파일을 FormData에 추가
  if (playground.value.profilePicture) {
    formData.append("imageFile", compressedImage);
  }


  try {
    const response = await axios.post(`${apiBaseUrl}/playground/add`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': getAccessToken()
      },
    })
    alert("成功")

  } catch (e) {
    const errorMessage = extractErrorMessage(error);
    alert(errorMessage);
  }
};

const compressImage = async (file) => {
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = new Image();
      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        canvas.width = 800; // 원하는 최대 너비
        canvas.height = (canvas.width * img.height) / img.width;

        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

        canvas.toBlob(
            (blob) => {
              resolve(new File([blob], file.name, { type: 'image/jpeg', lastModified: Date.now() }));
            },
            'image/jpeg',
            0.6 // 0 ~ 1 사이의 압축 품질
        );
      };
      img.src = e.target.result;
    };
    reader.readAsDataURL(file);
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
.profile-image-playground {
  width: 400px;
  height: 200px;
  background-color: #eee;
  aspect-ratio: 18 / 12; /* 18:9 비율로 설정 */
  object-fit: cover; /* 이미지가 지정된 비율에 맞도록 조정 */
  border: 2px solid var(--text-hint-light);
  display: inline-block;
}


</style>