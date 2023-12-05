<template>

  <div class="mx-auto max-w-md space-y-6">
    <div class="space-y-2 text-center">
      <h1 class="text-3xl font-bold">Sign In</h1>
      <p class="text-gray-500 dark:text-gray-400">Enter your email and password to sign in</p>
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
        <el-input class="custom-input" v-model="id" placeholder="Please input id" />

      <div class="space-id">
        <label
          class="label-password font-medium"
               for="password"
        >
          Password
        </label>
      </div>
        <el-input class="custom-input" v-model="password" placeholder="Please input password" />

      <button
          class="button-margin rounded-md text-sm font-medium ring-offset-background  fn=g focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 w-full"
          type="submit"
      >
        Sign In
      </button>
    </div>
    <div class="flex justify-between text-sm font-light">
      <a class="text-color-black font-light" href="#">
        Forgot password?
      </a>
      <a class="text-color-black font-light" href="#">
        Create an account
      </a>
    </div>

    <div data-orientation="horizontal" role="none" class="shrink-0 bg-gray-100 h-[1px] w-full my-8"></div>
    <div class="space-y-4">
      <hr class="margin-bottom">
      <button class="button-margin items-center justify-center rounded-md  font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-full">
        Sign in with WeChat
      </button>

      <button class="button-margin  items-center justify-center rounded-md font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-full">
        Sign in with QQ
      </button>
    </div>
  </div>
</template>

<script>


</script>

<style>
/* General Styles */
body {
  font-family: 'Arial', sans-serif;
  background-color: #f5f5f5;
  color: #333;
}

/* Container Styles */
.mx-auto {
  margin-left: auto;
  margin-right: auto;
}

.max-w-md {
  max-width: 20rem;
}

.space-y-6 {
  margin-top: 1.5rem;
  margin-bottom: 1rem;
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

.font-light {
  font-weight: 10;
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
.inline-flex {
  display: inline-flex;
}

.items-center {
  align-items: center;
}

.justify-center {
  justify-content: center;
}

.rounded-md {
  border-radius: 0.375rem;
}



button {
  background: black;
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
.border {
  border-width: 1px;
}






</style>