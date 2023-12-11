<!--
// v0 by Vercel.
// https://v0.dev/t/DQ2gFItZLqb
-->
<template>
<div class="space-y-6">
<div class="rounded-lg border bg-card text-card-foreground shadow-sm w-full max-w-lg" data-v0-t="card">
  <div class="flex flex-col space-y-1.5 p-4">
    <h3 class="text-2xl font-semibold leading-none tracking-tight">Time Selection</h3>
    <p class="text-sm text-muted-foreground">Select the start time and duration for your participation.</p>
  </div>
  <div class="p-4 border-t">
    <div class="grid gap-4">
      <button
          class="inline-flex items-center rounded-md text-sm ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-[300px] justify-start text-left font-normal"
          id="start-time"
          type="button"
          aria-haspopup="dialog"
          aria-expanded="false"
          aria-controls="radix-:rc:"
          data-state="closed"
      >
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
            class="mr-1 h-4 w-4 -translate-x-1"
        >
          <circle cx="12" cy="12" r="10"></circle>
          <polyline points="12 6 12 12 16 14"></polyline>
        </svg>
        Choose Start Time
      </button>
      <button
          class="inline-flex items-center rounded-md text-sm ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-[300px] justify-start text-left font-normal"
          id="duration"
          type="button"
          aria-haspopup="dialog"
          aria-expanded="false"
          aria-controls="radix-:rd:"
          data-state="closed"
      >
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
            class="mr-1 h-4 w-4 -translate-x-1"
        >
          <path d="M5 22h14"></path>
          <path d="M5 2h14"></path>
          <path d="M17 22v-4.172a2 2 0 0 0-.586-1.414L12 12l-4.414 4.414A2 2 0 0 0 7 17.828V22"></path>
          <path d="M7 2v4.172a2 2 0 0 0 .586 1.414L12 12l4.414-4.414A2 2 0 0 0 17 6.172V2"></path>
        </svg>
        Choose Duration
      </button>
    </div>
  </div>
  <div class="flex items-center p-4">
    <button class="inline-flex items-center justify-center rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2">
      Cancel
    </button>
    <button class="inline-flex items-center justify-center rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 ml-auto">
      Confirm
    </button>
  </div>
</div>
<div class="rounded-lg border bg-card text-card-foreground shadow-sm w-full max-w-lg" data-v0-t="card">
  <div class="flex flex-col space-y-1.5 p-4">
    <h3 class="text-2xl font-semibold leading-none tracking-tight">Occupied Time Slots</h3>
    <p class="text-sm text-muted-foreground">These slots are already taken. Please select a different time.</p>
  </div>
  <div class="p-4 border-t">
    <div class="space-y-2">
      <div
          class="rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 inline-flex items-center"
          color="red"
      >
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
            class="mr-1 h-4 w-4 -translate-x-1"
        >
          <circle cx="12" cy="12" r="10"></circle>
          <polyline points="12 6 12 12 16 14"></polyline>
        </svg>
        10:00 AM - 11:00 AM
      </div>
      <div
          class="rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 inline-flex items-center"
          color="red"
      >
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
            class="mr-1 h-4 w-4 -translate-x-1"
        >
          <circle cx="12" cy="12" r="10"></circle>
          <polyline points="12 6 12 12 16 14"></polyline>
        </svg>
        02:00 PM - 03:00 PM
      </div>
    </div>
  </div>
</div>
</div>
</template>
<style>
.rounded-md {
  border-radius: 0.375rem;
}

.text-sm {
  font-size: 0.875rem;
}

.focus-visible\:outline-none:focus {
  outline: none;
}

.transition-colors {
  transition: color 0.3s ease-in-out, background-color 0.3s ease-in-out;
}

.ring-offset-background {
  --ring-offset-color: #f3f4f6; /* You can customize the ring offset color */
}

/* Card Styles */
.bg-card {
  background-color: #ffffff; /* Change to your desired card background color */
}

.text-card-foreground {
  color: #1f2937; /* Change to your desired card text color */
}

.shadow-sm {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.max-w-lg {
  max-width: 24rem; /* Adjust the maximum width of the card */
}

/* Primary Button Styles */
.bg-primary {
  background-color: #2563eb; /* Change to your desired primary button color */
}

.text-primary-foreground {
  color: #ffffff; /* Change to your desired primary button text color */
}

.hover\:bg-primary:hover {
  background-color: #1d4ed8; /* Change to your desired primary button hover color */
}

/* Cancel Button Styles */
.hover\:bg-accent:hover {
  background-color: #f3f4f6; /* Change to your desired cancel button hover color */
}

.hover\:text-accent-foreground:hover {
  color: #6b7280; /* Change to your desired cancel button text hover color */
}

/* Time Slots Styles */
.rounded-full {
  border-radius: 9999px; /* Ensure a rounded shape for the slots */
}

/* Occupied Time Slots Styles */
[color="red"] {
  border-color: #e53e3e; /* Change to your desired occupied slot border color */
  color: #e53e3e; /* Change to your desired occupied slot text color */
}

</style>