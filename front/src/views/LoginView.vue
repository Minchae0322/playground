<template>
  <div class="login-container">
    <div class="login-container-2">
      <div class=" text-center">
        <h1 class="text-3xl">Sign In</h1>
        <div class="font" style="color: var(--text-hint)">Enter your email and password to sign in</div>
      </div>
      <div class="space-y-4">
        <div class="space-id">
          <label
              class="label-id font-medium"
              for="id"
          >
            Id
          </label>
        </div>
        <el-input class="custom-input" v-model="username" placeholder="Please input id"/>

        <div class="space-id">
          <label
              class="label-password font-medium"
              for="password"
          >
            Password
          </label>
        </div>
        <el-input class="custom-input" v-model="password" placeholder="Please input password"/>

        <button
            class="button-margin rounded-md text-sm font-medium ring-offset-background  fn=g focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 w-full"
            type="submit"
            @click="write"
        >
          Sign In
        </button>
      </div>
      <div class="flex justify-between text-sm font-light">
        <a class="text-color-black font" href="#">
          Forgot password?
        </a>
        <a class="text-color-black font" href="#">
          Create an account
        </a>
      </div>

      <div data-orientation="horizontal" role="none" class="shrink-0 bg-gray-100 h-[1px] w-full my-8"></div>
      <div class="space-y-4">
        <hr class="margin-bottom">
        <img

            src="../assets/logo_naver.png"
            alt="Your Image Alt Text"
            class="image-margin"
            width="50"
            height="50"
            @click="login_naver"
        />

        <img
            src="../assets/icon-github.png"
            alt="Your Image Alt Text"
            class="image-margin"
            width="50"
            height="50"
            @click="login_github"
        />
      </div>
    </div>
  </div>

</template>

<script setup lang="js">
import axios from "axios";
import {ref, onMounted, getCurrentInstance} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const username = ref("")
const password = ref("");
const internalInstance = getCurrentInstance();
const apiBaseUrl = internalInstance.appContext.config.globalProperties.$apiBaseUrl;

axios.defaults.withCredentials = true; // withCredentials 전역 설정

const login_naver = function () {
  try {
    window.location.href = `${apiBaseUrl}/oauth2/authorization/naver`;
  } catch (error) {
    console.error('Error during Naver login:', error);
    router.replace('/login'); // Redirect to /login in case of an error
  }
}

const login_github = function () {
  try {
    window.location.href = `${apiBaseUrl}/oauth2/authorization/github`;
  } catch (error) {
    console.error('Error during Github login:', error);
    router.replace('/login'); // Redirect to /login in case of an error
  }
};

const write = async function () {
  try {
    const response = await axios.post(`${apiBaseUrl}/auth/login`, {
      username: username.value,
      password: password.value
    }, {
      headers: {}
    });

    if (response.status === 200) {
    }
  } catch (error) {
    console.error('에러 발생:', error);
    alert("아이디 또는 비밀번호가 틀렸습니다.");
  }
}

</script>

<style scoped>
.login-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-content: center;
}

.login-container-2 {
  margin-top: 80px;
}


.font {
  font-family: MiSans-Light, sans-serif;
}


/* Form Styles */
.text-center {
  text-align: center;
}

.text-3xl {
  font-size: 1.875rem;
}

.justify-between {
  display: flex;
  justify-content: space-between;
}

.font-bold {
  font-weight: bold;
}

.text-color-black {
  color: black;
}

.custom-input {
  height: 2.3rem; /* 높이를 원하는 값으로 조절하세요. */

}

.image-margin {
  margin: 10px 10px 10px;
}

.button-margin {
  margin-top: 20px;
  margin-bottom: 10px;
}

.text-sm {
  font-size: 0.875rem;
}

.font-medium {
  font-weight: 490;
}

.margin-bottom {
  margin-bottom: 1.5rem;
}


.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}


.space-id {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.space-y-4 {
  padding-top: 0.5rem;
  padding-bottom: 1rem;
}

/* Button Styles */


.justify-center {
  justify-content: center;
}

.rounded-md {
  border-radius: 0.375rem;
}


button {
  background: #000000;
  color: white;
}


.h-10 {
  height: 2.5rem;
}

.px-4 {
  padding-left: 1rem;
  padding-right: 1rem;
}

.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.w-full {
  width: 100%;
}

/* Link Styles */


/* Separator Styles */
.shrink-0 {
  flex-shrink: 0;
}


.my-8 {
  margin-top: 2rem;
  margin-bottom: 2rem;
}

/* Social Sign-in Button Styles */


</style>