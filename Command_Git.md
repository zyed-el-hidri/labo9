# Git Commands Cheat Sheet

## Configuration
```bash
git config --global user.name "Your Name"        # Configure username
git config --global user.email "email@domain.com" # Configure email
git config --list                                # List all configuration
```

## Creating/Cloning Repositories
```bash
git init                                        # Initialize a local repository
git clone https://github.com/user/repo.git      # Clone a remote repository
```

## Basic Workflow
```bash
git status                                      # Check repository status
git add <file>                                  # Add file to staging area
git add .                                       # Add all files to staging area
git commit -m "Commit message"                  # Commit staged changes
git log                                         # View commit history
```

## Working with Branches
```bash
git branch                                      # List all local branches
git branch -a                                   # List all branches (local and remote)
git branch <branch-name>                        # Create a new branch
git checkout <branch-name>                      # Switch to a branch
git checkout -b <branch-name>                   # Create and switch to a new branch
git merge <branch-name>                         # Merge branch into current branch
git branch -d <branch-name>                     # Delete a branch
```

## Remote Repositories
```bash
git remote -v                                   # List remote repositories
git remote add origin <url>                     # Add a remote repository
git push -u origin <branch>                     # Push to remote and set upstream
git pull                                        # Pull changes from remote
git fetch                                       # Fetch changes without merging
```

## Undoing Changes
```bash
git restore <file>                              # Discard changes in working directory
git restore --staged <file>                     # Unstage a file
git reset --hard HEAD                           # Discard all local changes
git reset --hard HEAD~1                         # Undo last commit
git commit --amend                              # Modify the last commit
```

## Stashing
```bash
git stash                                       # Stash changes
git stash pop                                   # Apply and remove stashed changes
git stash list                                  # List all stashes
```

## Advanced
```bash
git rebase <branch>                             # Rebase current branch onto another
git cherry-pick <commit-hash>                   # Apply a specific commit to current branch
git tag <tag-name>                              # Create a tag
git diff                                        # Show changes between commits, commit and working tree, etc.
```
