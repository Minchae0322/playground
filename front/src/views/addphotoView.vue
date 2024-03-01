<template>
  <div class="user-profile-container-userInfo">
    <input type="file" ref="fileInput" @change="handleFileChange" />
    <div class="profile-hint">点击照片更改头像</div>
  </div>
</template>


<script setup>
import {getCurrentInstance, onMounted, ref} from "vue";
import axios from "axios";
import defaultImage from '../assets/img.png';
import {useRouter} from "vue-router";


const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;
const router = useRouter();
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