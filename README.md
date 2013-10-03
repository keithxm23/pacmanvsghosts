# Goldfish Guard Ghosts - My take on a stealth-styled Pacman game!
### Author: Keith Mascarenhas

## Custom AI Premise:
“Pacman is being hounded by the ghosts of Goldfish security Guards with walkie-talkies. As a result, they each have their own region of surveillance which they constantly patrol. However as soon as one of them gets sight of Pacman, they notify all the others of Pacman's last seen whereabouts and they all head towards the target. However, being the ghosts of goldfishes, they have very short memories (2-3 seconds) and once they’ve lost sight of Pacman for more than 3 seconds they forget all about him and get back to guarding their precious power-pills.”

### View the attached pdf for more details about the Traditional AI implementation and Custom AI implementation.

## Instructions:
To run my implementation of the traditional ghost behavior, have Executor.java point to 'MyGhosts()'
To run my custom AI for the ghost behavior, have Executor.java point to 'GhostGuards()' To run this, the GhostState.java file should also live in the same path as GhostGuards.java

## Research References:
http://pacman.shaunew.com/play/index.htm#learn
http://home.comcast.net/~jpittman2/pacman/pacmandossier.html#CH2_Scatter_Chase_Repeat
http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior