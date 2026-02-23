import { Config } from "jest";

const config: Config = {
  rootDir: ".",
  preset: "ts-jest",
  testEnvironment: "jsdom",
  setupFilesAfterEnv: ["<rootDir>/jest.setup.ts"],
  moduleNameMapper: {
    "^.+\\.module\\.(css|sass|scss)$": "identity-obj-proxy",
    "^.+\\.(css|sass|scss)$": "identity-obj-proxy",
    "^@/ui/(.*)$": "<rootDir>/app/_ui/$1",
    "^@/(.*)$": "<rootDir>/$1"
  },
  coveragePathIgnorePatterns: ["/node_modules", "/.next/", "/contexts/"],
  transformIgnorePatterns: ["/node_modules/", "/.next/"]
};

export default config;
