# Ray Tracing project

This is a project developed by Matteo Picciolini and Francesco Villa to reproduce (we hope!) a 2-D photorealistic image by the end of the course we are attending in Unimi, called *Numeric calculus for photorealistic images generation*.

We're coding in Java, in order to improve our skills in that cornerstone of code lenguadge.

The aim of the code deal with resolving the rendering equation given by the light inciding on some kind of surfaces, and we'll do it numerically, using dome montecarlo methods to resolve those integrals. 

The rendering equation:

$$
\begin{aligned}
L(x \rightarrow \Theta) = &amp;L_e(x \rightarrow \Theta) +
&amp;\int_{\Omega_x} f_r(x, \Psi \rightarrow \Theta)\,L(x \leftarrow
\Psi)\,\cos(N_x, \Psi)\,\mathrm{d}\omega_\Psi,
\end{aligned}
$$

So far, we have implemented the following topics

-   Color class:
    -   Class used to set the color of a single pixel of an image. It contains many methods as sum, product and scalar product between two pixels RGB triad.
 
-   HDR image class:
    -    Class used to build an HDR image with the following public variables: number of rows & columns and the intensity of the three colors RGB for every pixel.
    -    PfmCreator Class used to build the read_pfm_image function containing 4 sub-functions used in the main function.


we're going to upthate this repository gradually until the end of June. 