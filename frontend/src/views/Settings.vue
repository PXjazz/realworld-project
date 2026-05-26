<script setup lang="ts">
import { reactive, watch } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const form = reactive({
  image: '',
  username: '',
  bio: '',
  email: '',
  password: ''
})

const errors = reactive<Record<string, string>>({})

watch(
  () => userStore.currentUser,
  (user) => {
    if (user) {
      form.image = user.image || ''
      form.username = user.username
      form.bio = user.bio || ''
      form.email = user.email
      form.password = ''
    }
  },
  { immediate: true }
)

async function onSubmit() {
  Object.keys(errors).forEach(k => delete errors[k])

  if (!form.email) {
    errors.email = 'email is required'
  }
  if (!form.username) {
    errors.username = 'username is required'
  }
  if (Object.keys(errors).length > 0) return

  try {
    await userStore.updateUser({
      user: {
        email: form.email,
        username: form.username,
        bio: form.bio || null,
        image: form.image || null,
        password: form.password || undefined
      }
    })
  } catch (e: any) {
    const msg = e?.response?.data?.message || 'update failed'
    errors._server = msg
  }
}

function onLogout() {
  userStore.logout()
}
</script>

<template>
  <div class="settings-page">
    <div class="container page">
      <div class="row">
        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Your Settings</h1>

          <ul v-if="Object.keys(errors).length" class="error-messages">
            <li v-for="(msg, field) in errors" :key="field">{{ field }} {{ msg }}</li>
          </ul>

          <form @submit.prevent="onSubmit">
            <fieldset class="form-group">
              <input
                v-model="form.image"
                class="form-control form-control-lg"
                type="text"
                placeholder="URL of profile picture"
              />
            </fieldset>
            <fieldset class="form-group">
              <input
                v-model="form.username"
                class="form-control form-control-lg"
                type="text"
                placeholder="Username"
              />
            </fieldset>
            <fieldset class="form-group">
              <textarea
                v-model="form.bio"
                class="form-control form-control-lg"
                rows="8"
                placeholder="Short bio about you"
              ></textarea>
            </fieldset>
            <fieldset class="form-group">
              <input
                v-model="form.email"
                class="form-control form-control-lg"
                type="email"
                placeholder="Email"
              />
            </fieldset>
            <fieldset class="form-group">
              <input
                v-model="form.password"
                class="form-control form-control-lg"
                type="password"
                placeholder="New Password"
              />
            </fieldset>
            <button
              class="btn btn-lg btn-primary pull-xs-right"
              type="submit"
            >
              Update Settings
            </button>
          </form>

          <hr />

          <button class="btn btn-outline-danger" @click="onLogout">
            Or click here to logout.
          </button>
        </div>
      </div>
    </div>
  </div>
</template>