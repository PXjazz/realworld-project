<script setup lang="ts">
import { reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { RouterLink } from 'vue-router'

const userStore = useUserStore()

const form = reactive({
  email: '',
  password: ''
})

const errors = reactive<Record<string, string>>({})

async function onSubmit() {
  Object.keys(errors).forEach(k => delete errors[k])

  if (!form.email) {
    errors.email = 'email is required'
  }
  if (!form.password) {
    errors.password = 'password is required'
  }
  if (Object.keys(errors).length > 0) return

  try {
    await userStore.login({ user: form })
  } catch (e: any) {
    const msg = e?.response?.data?.message || 'login failed'
    errors._server = msg
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="container page">
      <div class="row">
        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Sign in</h1>
          <p class="text-xs-center">
            <RouterLink to="/register">Need an account?</RouterLink>
          </p>

          <ul v-if="Object.keys(errors).length" class="error-messages">
            <li v-for="(msg, field) in errors" :key="field">{{ field }} {{ msg }}</li>
          </ul>

          <form @submit.prevent="onSubmit">
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
                placeholder="Password"
              />
            </fieldset>
            <button
              class="btn btn-lg btn-primary pull-xs-right"
              :disabled="userStore.loading"
            >
              Sign in
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
