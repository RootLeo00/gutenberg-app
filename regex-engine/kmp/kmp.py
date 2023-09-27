# Python program for KMP Algorithm
def KMPSearch(pattern, txt):
	M = len(pattern)
	N = len(txt)

	# create lps[] that will hold the longest prefix suffix
	# values for patterntern
	
	j = 0 # index for pattern[]

	# Preprocess the pattern (calculate lps[] array)
	lps=computeLPSArray(pattern, M)
	lps=computeCarryOverArray(pattern, M, lps)

	i = 0 # index for txt[]
	while i < N:
		if pattern[j] == txt[i]:
			i += 1
			j += 1

		if j == M:
			print ("Found pattern at index", str(i-j))
			j = lps[j-1]

		# mismatch after j matches
		elif i < N and pattern[j] != txt[i]:
			# Do not match lps[0..lps[j-1]] characters,
			# they will match anyway
			if j != 0:
				j = lps[j-1]
			else:
				i += 1

def computeLPSArray(pattern, M):
	len = 0 # length of the previous longest prefix suffix
	lps = [0]*M

 
	i = 1
 
    # the loop calculates lps[i] for i = 1 to M-1
	# agtagtc
	while i < M:
		if pattern[i]== pattern[len]:
			len += 1
			lps[i] = len
			i += 1
		else:
			if len != 0:
				len = lps[len-1]
			else:
				lps[i] = 0
				i += 1
	#shift all the array by one position forward and assign lps[0] =-1
	lps.insert(0, -1)
	return lps


def computeCarryOverArray(pattern, M, lps):
	"""
		This is an optimization of the computeLPSArray function
        :param pattern: patterntern, M: length of patterntern, lps: longest prefix suffix        
    """

	# the loop calculates lps[i] for i = 0 to M-1
	for i in range(1, M):
		if pattern[i]== pattern[lps[i]]:
			if lps[lps[i]] == -1:
				lps[i] = -1
			else:
				lps[i] = lps[lps[i]]
		else:
			lps[i]=lps[i]
	return lps


#test
txt = "ABABDABACDABABCABAB"
pattern = ["ABABCABAB", "agtagtc"]

lps=computeLPSArray("mamamia", len("mamamia"))
print("lps", lps)
lps=computeCarryOverArray("mamamia", len("mamamia"), lps)
print("carryover", lps)
KMPSearch(pattern[0], txt)