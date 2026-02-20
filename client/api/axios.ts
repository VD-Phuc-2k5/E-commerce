import axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import APIResponse from "@/api/response";

class APIClient {
  private instance: AxiosInstance;
  constructor() {
    this.instance = axios.create({
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      timeout: 5000,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json"
      }

      // TO DO: Setup interceptors
    });
  }

  // Get the axios instance for advanced usage
  public getInstance(): AxiosInstance {
    return this.instance;
  }

  // GET request
  public async get<T = any>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<APIResponse<T>> {
    try {
      const response = await this.instance.get<APIResponse<T>>(url, config);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // POST request
  public async post<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<APIResponse<T>> {
    try {
      const response = await this.instance.post<APIResponse<T>>(
        url,
        data,
        config
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Update base url
  public setBaseUrl(url: string) {
    this.instance.defaults.baseURL = url;
  }
}

const apiClient = new APIClient();
export default apiClient;
