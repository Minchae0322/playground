<template>
  <body>


  <div class="bg-[#f3f4f6]  item-center min-h-screen p-8">
    <div class="max-w-4xl mx-auto">
      <button
          type="button"
          role="combobox"
          aria-controls="radix-:r1a:"
          aria-expanded="false"
          aria-autocomplete="none"
          dir="ltr"
          data-state="closed"
          data-placeholder=""
          class="flex h-10  items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          id="category"
      >
        <span style="pointer-events: none;">Select a category</span>
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="h-4 w-4 opacity-50"
            aria-hidden="true"
        >
          <path d="m6 9 6 6 6-6"></path>
        </svg>
      </button>
      <div class="mt-6 items-center   ">
        <h2 class="text-xl font-bold mb-4 ">Playground Item 1</h2>
        <img
            src="../assets/26759.jpg"
            alt="Playground"
            class=" h-auto bg-cover rounded-md padding background-color "
            width="1200"
            height="400"
            style="aspect-ratio: 1600 / 400; object-fit: cover;"
        />
        <div class="mt-4">
          <div class="bg-[#34d399] p-4 rounded-md shadow-md">
            <p class="text-white font-semibold">10:00am - 11:00am</p>
            <p class="text-white mt-2">John Doe</p>
          </div>
        </div>
      </div>
    </div>
  </div>
  </body>
</template>

<style scoped>
body {
  font-family: 'Arial', sans-serif;
  background-color: #f3f4f6;

}

/* Container Styles */


.min-h-screen {
  min-height: 100vh;
}

.p-8 {
  padding: 2rem;
}

.justify-center {
  justify-content: center;
}

/* Button Styles */
#category {
  /* Add your button styles here */
}

.padding {
  padding: 30px;
}
/* Playground Item Styles */
.mt-6 {
  margin-top: 1.5rem;
}
.background-color {
  background: rgba(51, 51, 51, 0.36);
}
.text-xl {
  font-size: 1.25rem;
}

.font-bold {
  font-weight: bold;
}

.mb-4 {
  margin-bottom: 1rem;
}

/* Image Styles */
.w-full {
  width: 100%;

}

.item-center {
  align-items: center;
}

.h-auto {
  height: auto;
}

.bg-cover {
  background-size: cover;
}

/* Playground Item Details Styles */
.mt-4 {
  margin-top: 1rem;
}

.bg-[#34d399] {
  background-color: #34d399;
}

.p-4 {
  padding: 1rem;
}

.rounded-md {
  border-radius: 0.375rem;
}

.shadow-md {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.text-white {
  color: #fff;
}

.font-semibold {
  font-weight: 600;
}

.mt-2 {
  margin-top: 0.5rem;
}

/* Additional Styles */
.w-soccer {
  margin: 30px;
}
</style>
