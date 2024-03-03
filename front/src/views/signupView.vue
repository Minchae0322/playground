<template>
  <div class="signup-container">

    <form class="signup-form">
      <h2>登记</h2>

      <div class="form-group">
        <label for="username">账号</label>
        <input
            type="text"
            id="username"
            v-model="formData.username"
            @input="validateUsername"
            required
        />
        <span v-if="usernameError" class="error-message">{{ usernameError }}</span>
      </div>



      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="formData.password" required>
      </div>

      <div class="form-group">
        <label for="confirmPassword">确认密码</label>
        <input type="password" id="confirmPassword" v-model="formData.confirmPassword" required>
        <span v-if="passwordsMismatch" class="error-message">密码不一致</span>
      </div>

      <button type="submit" @click="submitForm">确认</button>
    </form>
  </div>
</template>

<script setup>
import {getCurrentInstance, ref, watch} from 'vue';
import axios from "axios";
import {useRouter} from "vue-router";

const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

axios.defaults.withCredentials = true; // withCredentials 전역 설정
const router = useRouter();
const formData = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

const passwordsMismatch = ref(false);
const usernameError = ref('');
const submitForm = async () => {
  if (formData.value.password !== formData.value.confirmPassword) {
    passwordsMismatch.value = true;
    alert("密码不一致")
    return; // 비밀번호가 일치하지 않으면 폼 제출을 중단합니다.
  }

  try {

    const response = await axios.post(`${apiBaseUrl}/user/signup`, {
      username: formData.value.username,
      password: formData.value.password,
    }, {});

    alert("登记成功")
  } catch (e) {
    alert(e.response.data.message)
  }
  await router.replace('/login')

  // 여기에 서버로 데이터를 보내거나 다른 작업을 수행하는 로직을 추가하세요.
  console.log('Form submitted with data:', formData.value.confirmPassword);
};

const validateUsername = async () => {
  const regex = /^[a-zA-Z0-9]{1,11}$/;
  if (!regex.test(formData.value.username)) {
    usernameError.value = 'dddddddddd';
  } else {
    usernameError.value = '';
  }
};

// 비밀번호 확인 값이 변경될 때마다 불일치 여부를 업데이트합니다.
watch(() => formData.confirmPassword, () => {
  passwordsMismatch.value = false;
});

</script>


<style scoped>
.signup-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.signup-form {
  max-width: 400px;
  width: 100%;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #333;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

button {
  border-radius: 8px;
  background: #000000;
  color: white;
}

font {
  font-family: MiSans-Light, sans-serif;
}
.error-message {
  font-family: MiSans-Normal, sans-serif ;
  color: #ff0000;
  font-size: 14px;
  margin-top: 4px;
}

</style>