package com.example.crowdfundingplatform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

    public final class SslKeystoreBootstrap {

        private static final Logger log = LoggerFactory.getLogger(SslKeystoreBootstrap.class);
        private static final String DEFAULT_KEYSTORE_PATH = "ssl/crowdfunding-platform.jks";
        private static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";
        private static final String DEFAULT_ALIAS = "crowdfunding-platform";
        private static final String DEFAULT_DNAME = "CN=localhost, OU=Dev, O=CrowdfundingPlatform, L=Local, S=Local, C=AR";

        private SslKeystoreBootstrap() {
        }

        public static void ensureKeystoreExists() {
            if (!isSslEnabled()) {
                return;
            }

            Path keystorePath = resolveKeystorePath(System.getenv("SSL_KEY_STORE"));
            String storePassword = valueOrDefault(System.getenv("SSL_KEY_STORE_PASSWORD"), DEFAULT_KEYSTORE_PASSWORD);
            String keyPassword = valueOrDefault(System.getenv("SSL_KEY_PASSWORD"), storePassword);
            String alias = valueOrDefault(System.getenv("SSL_KEY_ALIAS"), DEFAULT_ALIAS);
            String keystoreType = valueOrDefault(System.getenv("SSL_KEY_STORE_TYPE"), "JKS");

            try {
                if (Files.exists(keystorePath) && Files.size(keystorePath) > 0) {
                    log.info("Using existing SSL keystore at {}", keystorePath.toAbsolutePath());
                    return;
                }

                Files.createDirectories(keystorePath.getParent());
                runKeytool(keystorePath, storePassword, keyPassword, alias, keystoreType);
                log.info("Generated self-signed SSL keystore at {}", keystorePath.toAbsolutePath());
            } catch (IOException exception) {
                throw new IllegalStateException("Unable to prepare SSL keystore", exception);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("SSL keystore generation was interrupted", exception);
            }
        }

        private static boolean isSslEnabled() {
            return Boolean.parseBoolean(valueOrDefault(System.getenv("SSL_ENABLED"), "true"));
        }

        private static Path resolveKeystorePath(String configuredPath) {
            String pathValue = configuredPath == null || configuredPath.isBlank() ? DEFAULT_KEYSTORE_PATH : configuredPath;
            return Paths.get(pathValue).toAbsolutePath().normalize();
        }

        private static String valueOrDefault(String value, String defaultValue) {
            return value == null || value.isBlank() ? defaultValue : value;
        }

        private static void runKeytool(Path keystorePath,
                                       String storePassword,
                                       String keyPassword,
                                       String alias,
                                       String keystoreType) throws IOException, InterruptedException {
            String keytoolExecutable = resolveKeytoolExecutable();

            List<String> command = List.of(
                    keytoolExecutable,
                    "-genkeypair",
                    "-alias", alias,
                    "-keyalg", "RSA",
                    "-keysize", "2048",
                    "-validity", "3650",
                    "-storetype", keystoreType,
                    "-keystore", keystorePath.toString(),
                    "-storepass", storePassword,
                    "-keypass", keyPassword,
                    "-dname", DEFAULT_DNAME,
                    "-noprompt"
            );

            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();

            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException("keytool failed with exit code " + exitCode + ": " + output);
            }
        }

        private static String resolveKeytoolExecutable() {
            String javaHome = System.getProperty("java.home");
            Path keytoolPath = Paths.get(javaHome, "bin", isWindows() ? "keytool.exe" : "keytool");
            if (Files.isExecutable(keytoolPath)) {
                return keytoolPath.toString();
            }

            return isWindows() ? "keytool.exe" : "keytool";
        }

        private static boolean isWindows() {
            return System.getProperty("os.name").toLowerCase().contains("win");
        }
    }

