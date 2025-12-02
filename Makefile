# =====================================
# Global config
# =====================================
MVN = mvn
JAVA = java

# Folder project
PLAYER_DIR = player

# Nama hasil build
PLAYER_JAR = $(PLAYER_DIR)/target/quizmath_player-1.0-SNAPSHOT.jar

# =====================================
# Default target
# =====================================
.PHONY: all clean admin player

player:
	@echo ">>> Building Player Project..."
	@cd $(PLAYER_DIR) && $(MVN) clean package -q
	@echo ">>> Running Player..."
	@$(JAVA) -jar $(PLAYER_JAR)

all:
	@echo "Usage:"
	@echo "  make player  -> build & run Player app"
	@echo "  make clean   -> bersihin semua build"

# =====================================
# Build & Run Player
# =====================================


# =====================================
# Clean semua target
# =====================================
clean:
	@echo ">>> Cleaning projects..."
	@cd $(PLAYER_DIR) && $(MVN) clean -q
	@echo ">>> Done cleaning."
