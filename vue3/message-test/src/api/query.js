import request from '@/utils/request'
export const query = (data) => {
  return request.post('/query', JSON.stringify(data))
}
