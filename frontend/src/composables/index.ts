import { ref, type Ref } from 'vue'

export function useLoading() {
  const loading: Ref<boolean> = ref(false)

  function setLoading(value: boolean) {
    loading.value = value
  }

  return { loading, setLoading }
}
