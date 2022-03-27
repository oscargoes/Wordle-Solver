# Wordle-Solver

The idea behind this alogirithm is to use a brute force approach along with some basic frequency data of the position of characters in 5 letter words to 
curate a weighted list of words on each guessing turn. The list adjusts based on Wordle feedback and updates accordingly. Weights are adjusted so that 
words with repeated letters are not as valuable. The algorithm also accounts for situations when multiple of the same character are in a word.
